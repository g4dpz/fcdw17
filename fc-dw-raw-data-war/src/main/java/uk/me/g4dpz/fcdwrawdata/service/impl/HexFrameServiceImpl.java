package uk.me.g4dpz.fcdwrawdata.service.impl;

import uk.me.g4dpz.fcdwcommon.dto.HexFrameDTO;
import uk.me.g4dpz.fcdwcommon.utils.Clock;
import uk.me.g4dpz.fcdwrawdata.config.EnvConfig;
import uk.me.g4dpz.fcdwrawdata.domain.*;
import uk.me.g4dpz.fcdwrawdata.dao.*;
import uk.me.g4dpz.fcdwrawdata.service.HexFrameService;
import uk.me.g4dpz.fcdwrawdata.service.UserHexString;
import uk.me.g4dpz.fcdwrawdata.utils.ServiceUtility;
import uk.me.g4dpz.satpredict.client.SatPredictClient;
import uk.me.g4dpz.satpredict.client.dto.SatPosDTO;
import uk.me.g4dpz.satpredict.client.impl.SatPredictClientImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.me.g4dpz.fcdwrawdata.utils.ServiceUtility.convertHexBytePairToBinary;

@Service
public class HexFrameServiceImpl implements HexFrameService {


    private static final Logger LOG = LoggerFactory.getLogger(HexFrameServiceImpl.class);

    private static final Map<String, String> USER_AUTH_KEYS = new ConcurrentHashMap<>();

    private static final String HAD_INCORRECT_DIGEST = "] had incorrect digest";
    private static final String USER_WITH_SITE_ID = "User with site id [";
    private static final String NOT_FOUND = "] not found";
    private static final long TWO_DAY_SEQ_COUNT = 1440;
    private static final Map<Long, SatelliteStatus> SATELLITE_STATUS_MAP = new ConcurrentHashMap<Long, SatelliteStatus>();

    public HexFrameServiceImpl() {}

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HexFrameDao hexFrameDao;

    @Autowired
    private Clock clock;

    @Autowired
    private SatelliteStatusDao satelliteStatusDao;

    @Autowired
    private UserRankingDao userRankingDao;

    @Autowired
    private PayloadDao payloadDao;

    private SatPredictClient satPredictClient = null;

    public HexFrameServiceImpl(HexFrameDao hexFrameDao, UserDao userDao, Clock clock,
                               SatelliteStatusDao satelliteStatusDao, UserRankingDao userRankingDao,
                               EnvConfig envConfig,
                               PayloadDao payloadDao) {
        this.hexFrameDao = hexFrameDao;
        this.userDao = userDao;
        this.clock = clock;
        this.satelliteStatusDao = satelliteStatusDao;
        this.userRankingDao = userRankingDao;
        this.envConfig = envConfig;
        this.payloadDao = payloadDao;
    }

    @Override
    public String ping() {
        return "Hello";
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PacketResponseEntity processHexFrame(String siteId, String digest, String body) {

        final User user = userDao.findBySiteId(siteId);

        String authKey;

        if (user != null) {
            String hexString;
            if (body.contains("&")) {
                hexString = StringUtils.substringBetween(body, "=", "&");
            } else {
                hexString = body.substring(body.indexOf("=") + 1);
            }

            hexString = hexString.replace("+", " ");

            authKey = USER_AUTH_KEYS.get(siteId);


            try {

                if (authKey == null) {
                    authKey = user.getAuthKey();
                    USER_AUTH_KEYS.put(siteId, authKey);
                }

                LOG.debug("Using authKey: " + authKey);

                final String calculatedDigest = ServiceUtility.calculateDigest(hexString,
                        authKey, null);
                final String calculatedDigestUTF8 = ServiceUtility.calculateDigest(hexString,
                        authKey, Integer.valueOf(8));
                final String calculatedDigestUTF16 = ServiceUtility.calculateDigest(hexString,
                        authKey, Integer.valueOf(16));

                if (null != digest
                        && (digest.equals(calculatedDigest)
                        || digest.equals(calculatedDigestUTF8) || digest
                        .equals(calculatedDigestUTF16))) {

                    hexString = StringUtils.deleteWhitespace(hexString);

                    final Date now = new Date(5000 * (clock.currentDate().getTime() / 5000));

                    return processHexFrame(new UserHexString(user,
                            StringUtils.deleteWhitespace(hexString), now));
                } else {
                    LOG.error(USER_WITH_SITE_ID + siteId + HAD_INCORRECT_DIGEST
                            + ", received: " + digest + ", calculated: "
                            + calculatedDigest);
                    final ResponseEntity<String> responseEntity = new ResponseEntity<>("UNAUTHORIZED",
                            HttpStatus.UNAUTHORIZED);
                    return new PacketResponseEntity(responseEntity);
                }

            } catch (final Exception e) {
                LOG.error(e.getMessage());
                final ResponseEntity<String> responseEntity = new ResponseEntity<>(e.getMessage(),
                        HttpStatus.BAD_REQUEST);
                return new PacketResponseEntity(responseEntity);
            }

        } else {
            LOG.error("Site id: " + siteId + " not found in database");
            final ResponseEntity<String> responseEntity = new ResponseEntity<>("UNAUTHORIZED",
                    HttpStatus.UNAUTHORIZED);
            return new PacketResponseEntity(responseEntity);
        }
    }

    private PacketResponseEntity processHexFrame(UserHexString userHexString) {

        final List<SatelliteStatus> satelliteStatuses = satelliteStatusDao.findAll();

        for(SatelliteStatus satelliteStatus : satelliteStatuses) {
            SATELLITE_STATUS_MAP.put(satelliteStatus.getSatelliteId(), satelliteStatus);
        }

        final String hexString = userHexString.getHexString();
        final User user = userHexString.getUser();
        final Date createdDate = userHexString.getCreatedDate();


        String binary = convertHexBytePairToBinary(hexString);
        int firstByte = Integer.parseInt(binary.substring(0, 8), 2);
        int secondByte = Integer.parseInt(binary.substring(8, 16), 2);

        long satelliteId = ((firstByte & 192) >> 6);

        if (satelliteId == 3) {
            satelliteId += ((secondByte & 252));
        }

        long frameType = (firstByte & 63);

        long sequenceNumber = getSequenceNumber(satelliteId, hexString);

        LOG.info(String.format("Processing %d %d %d", satelliteId, sequenceNumber, frameType));

        if (!SATELLITE_STATUS_MAP.containsKey(satelliteId)) {
            final String noSatelliteStatusFound = String.format("No satelliteStatus found for satellite %d", satelliteId);
            LOG.error(noSatelliteStatusFound);
            final ResponseEntity<String> responseEntity = new ResponseEntity<>(noSatelliteStatusFound, HttpStatus.BAD_REQUEST);
            return new PacketResponseEntity(responseEntity);
        }

        /* ----------------------------------------
        Satellite no         Satellite ID            Satellite name
        decimal         Decimal   Binary      HEX
        0               0          00000000   00      FUNcube EM
        1               1          00000001   01      Ukube
        2               2          00000010   02      FUNcube-1
        3               3          00000011   03      EXTENDED PROTOCOL START
        4               7          00000111   07      ESEO
        5               11         00001011   0B      NAYIF-1
        6               15         00001111   0F      JY1SAT EM
        7               19         00010011   13      JY1SAT FM
         */

        // bomb out early if the upload is out of bounds
        if (satelliteId != 1) {

            final Long maxSequenceNumber = hexFrameDao
                    .getMaxSequenceNumber(satelliteId);

            if (maxSequenceNumber != null ) {
                final long diff = Math.abs(maxSequenceNumber - sequenceNumber);

                if ((maxSequenceNumber > sequenceNumber) && (diff > TWO_DAY_SEQ_COUNT)) {

                    final String message = String
                            .format("User %s loading sequence number %d is out of bounds for satelliteId %d",
                                    user.getSiteId(), sequenceNumber,
                                    satelliteId);
                    LOG.warn(message);

                    return new PacketResponseEntity(ResponseEntity.ok().build());
                }
            }

        }

        List<HexFrame> hexFrameEntities = new ArrayList<>();

        String payloadText = hexString.substring(112);
        String preamble = hexString.substring(0, 112);

        try {
            hexFrameEntities  = hexFrameDao.findBySatelliteIdAndSequenceNumberAndFrameType(satelliteId, sequenceNumber, frameType);
        }
        catch (Exception enfe) {
            final String message = enfe.getMessage();
            String[] msgParts = message.split(" ");
            long payloadId = Long.parseLong(msgParts[msgParts.length - 1]);
            if (message.contains("Unable to find") && message.contains("Payload with id")) {
                // does it exist with another id
                Payload payload = payloadDao.findByHexText(payloadText);
                LOG.error("Payload " + payloadId + " not found");
                if (payload != null) {
                    LOG.info("Was found with ID " + payload.getId());
                    int updated = setPayloadId(satelliteId, frameType, sequenceNumber, payload);
                    LOG.info("Updated: " + updated);
                    return new PacketResponseEntity(ResponseEntity.badRequest().build());
                }
                else {
                    Payload newPayload = new Payload();
                    newPayload.setId(payloadId);
                    newPayload.setHexText(payloadText);
                    newPayload.setCreatedDate(new Date(System.currentTimeMillis()));
                    newPayload = payloadDao.save(newPayload);
                    int updated = setPayloadId(satelliteId, frameType, sequenceNumber, payload);
                }

                hexFrameEntities  = hexFrameDao.findBySatelliteIdAndSequenceNumberAndFrameType(satelliteId, sequenceNumber, frameType);
            }
        }

        if (hexFrameEntities.isEmpty()) {

            Payload payload = payloadDao.findByHexText(payloadText);

            if (payload == null) {
                payload = new Payload();
                payload.setHexText(payloadText);
                payload.setCreatedDate(new Date(System.currentTimeMillis()));
                payload = payloadDao.save(payload);
            }

            LOG.info(String.format("Saving %d %d %d for %s", satelliteId, sequenceNumber, frameType, user.getSiteId()));
            HexFrame hexFrameEntity = new HexFrame(satelliteId, frameType, sequenceNumber, preamble, createdDate,
                    true, new Timestamp(createdDate.getTime()));
            hexFrameEntity.setOutOfOrder(isOutOfOrder(hexFrameEntity));
            hexFrameEntity.addUser(user);
            hexFrameEntity.setFitterProcessed(false);
            hexFrameEntity.setRealtimeProcessed(false);
            hexFrameEntity.setHighPrecisionProcessed(false);
            hexFrameEntity.setPayload(payload);
            incrementUploadRanking(satelliteId, user.getSiteId(), createdDate);

            SatelliteStatus satelliteStatus = SATELLITE_STATUS_MAP.get(satelliteId);

            if ((sequenceNumber == satelliteStatus.getSequenceNumber() && frameType > satelliteStatus.getFrameType())
                || (sequenceNumber > satelliteStatus.getSequenceNumber()))
            {
                satelliteStatus.setSequenceNumber(sequenceNumber);
                satelliteStatus.setFrameType(frameType);
                satelliteStatusDao.save(satelliteStatus);

                addSatellitePosition(hexFrameEntity, SATELLITE_STATUS_MAP.get(satelliteId).getCatalogueNumber());

            }
            else if (sequenceNumber == satelliteStatus.getSequenceNumber())
            {
                addSatellitePosition(hexFrameEntity, SATELLITE_STATUS_MAP.get(satelliteId).getCatalogueNumber());

            }
            else {
                hexFrameEntity.setLatitude("0.0");
                hexFrameEntity.setLongitude("0.0");
            }


            if (isAntatrtic(hexFrameEntity.getLatitude(), hexFrameEntity.getLongitude())) {
                incrementUploadRanking(satelliteId, "test1", createdDate);
            }

            hexFrameDao.save(hexFrameEntity);

            if (satelliteId == 0 && sequenceNumber == 0 && firstByte == 0) {
                LOG.warn("--- RESET ---");
                final ResponseEntity<String> responseEntity = new ResponseEntity<>("Satellite RESET", HttpStatus.BAD_REQUEST);
                return new PacketResponseEntity(responseEntity);
            }
        }
        else {
            HexFrame hexFrameEntity = hexFrameEntities.get(0);
            Iterator<User> userIterator = hexFrameEntity.getUsers().iterator();

            boolean userFound = false;

            while(userIterator.hasNext()) {
                User userEntity = userIterator.next();
                if (user.getSiteId().equals(userEntity.getSiteId())) {
                    userFound = true;
                    break;
                }
            }

            if (userFound) {
                LOG.info(String.format("User %s has already saved %d %d %d", user.getSiteId(), satelliteId, sequenceNumber, frameType));
                final ResponseEntity<String> responseEntity = new ResponseEntity<>("Already Reported", HttpStatus.OK);
                return new PacketResponseEntity(responseEntity);
            }
            else {
                String siteId = user.getSiteId();
                LOG.info(String.format("Adding user %s to %d %d %d", siteId, satelliteId, sequenceNumber, frameType));
                hexFrameEntity.addUser(user);
                incrementUploadRanking(satelliteId, user.getSiteId(), createdDate);
                hexFrameEntity = hexFrameDao.save(hexFrameEntity);

                // we do not want the controller to send the JMS message
                satelliteId = 0;
                sequenceNumber = 0;
                frameType = 0;
            }
        }

        final PacketResponseEntity packetResponseEntity = new PacketResponseEntity(ResponseEntity.ok().build());
        packetResponseEntity.setSatelliteId(satelliteId);
        packetResponseEntity.setSequenceNumber(sequenceNumber);
        packetResponseEntity.setFrameType(frameType);
        return packetResponseEntity;

    }

    private boolean isAntatrtic(String latitude, String longitude) {
        try {
            Double lat = Double.valueOf(latitude);
            Double lon = Double.valueOf(longitude);
            return (lat < -30.0 && (lon > 320 || lon < 60.0));
        }
        catch (NullPointerException npe) {
            return false;
        }
    }


    private int setPayloadId(long satelliteId, long frameType, long sequenceNumber, Payload payload) {
        return hexFrameDao.setPayload(payload, satelliteId, sequenceNumber, frameType);
    }

    private void incrementUploadRanking(long satelliteId, String siteId, Date createdDate) {
        final Timestamp latestUploadDate = new Timestamp(createdDate.getTime());

        final List<UserRanking> userRankings = userRankingDao
                .findBySatelliteIdAndSiteId(satelliteId, siteId);

        UserRanking userRanking;

        if (userRankings.isEmpty()) {

            userRanking = new UserRanking((long)satelliteId, siteId, 1L,
                    latestUploadDate, latestUploadDate);
        }
        else {
            userRanking = userRankings.get(0);
            userRanking.setLatestUploadDate(latestUploadDate);
            Long number = userRanking.getNumber();
            number++;
            userRanking.setNumber(number);
        }

        userRankingDao.save(userRanking);
    }




    private static long getSequenceNumber(long satelliteId, String hexString) {

        final String binaryString = convertHexBytePairToBinary(hexString
                .substring((satelliteId < 3) ? 2 : 4, hexString.length()));

        switch((int) satelliteId) {
            // FUNcube (EM/FM)
            case 0:
            case 2:
                return Long.parseLong(binaryString.substring(392, 392 + 24), 2);
            // UKube1
            case 1:
                return Long.parseLong(binaryString.substring(402, 402 + 24), 2);
            // ESEO
            case 7:
                return Long.parseLong(binaryString.substring(168, 168 + 24), 2);
            //Nayif
            case 11:
                // Jy1Sat_EM
            case 15:
                // Jy1Sat_FM
            case 19:
                return Long.parseLong(binaryString.substring(384, 384 + 24), 2);
            default:
                return -1L;
        }
    }

    private Boolean isOutOfOrder(HexFrame hexFrameEntity) {

        boolean outOfOrder = false;

        final List<HexFrame> existingFrames = hexFrameDao
                .findBySatelliteIdAndSequenceNumber(hexFrameEntity.getSatelliteId(),
                        hexFrameEntity.getSequenceNumber());

        if (!existingFrames.isEmpty()) {
            for (final HexFrame existingFrame : existingFrames) {
                if (existingFrame.getCreatedDate().before(hexFrameEntity.getCreatedDate())
                        && existingFrame.getFrameType() > hexFrameEntity.getFrameType()) {
                    outOfOrder = true;
                    break;
                }
            }
        }

        return outOfOrder;
    }

    private void addSatellitePosition(HexFrame hexFrameEntity, long catalogueNumber) {

        if (satPredictClient == null) {
            satPredictClient = new SatPredictClientImpl(envConfig.satpredictURL());
        }

        SatPosDTO satpos = satPredictClient.getPosition(catalogueNumber, 0.0,0.0,0.0);

        if (satpos != null) {
            final String latitude = satpos.getLatitude();
            final String longitude = satpos.getLongitude();

            hexFrameEntity.setLatitude(latitude != null ? latitude : "0.0" );
            hexFrameEntity.setLongitude(longitude != null ? longitude : "0.0");
        }
        else {
            LOG.error("SATPREDICT_URL not set");
            hexFrameEntity.setLatitude("0.0");
            hexFrameEntity.setLongitude("0.0");
        }

        return;
    }

    @Override
    @JmsListener(destination = "frame_processed")
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public void handleMessage(final String message) {
        LOG.info("Message received: " + message);
    }

    public HexFrameDTO getFrame(long satelliteId, long sequenceNumber, long frameType) {

        final List<HexFrame> hexFrameList = hexFrameDao.findBySatelliteIdAndSequenceNumberAndFrameType(satelliteId, sequenceNumber, frameType);

        if (hexFrameList.isEmpty()) {
            LOG.warn(String.format("No frames found for %d %d %d", satelliteId, sequenceNumber, frameType));
            return null;
        }

        HexFrame hfe = hexFrameList.get(0);
        HexFrameDTO hexFrameDTO = new HexFrameDTO();
        hexFrameDTO.setSatelliteId(hfe.getSatelliteId());
        hexFrameDTO.setSequenceNumber(hfe.getSequenceNumber());
        hexFrameDTO.setFrameType(hfe.getFrameType());
        hexFrameDTO.setHexString(hfe.getHexString());
        hexFrameDTO.setCreatedDate(hfe.getCreatedDate());
        hexFrameDTO.setLatitude(hfe.getLatitude());
        hexFrameDTO.setLongitude(hfe.getLongitude());
        hexFrameDTO.setSatelliteTime(hfe.getSatelliteTime());

        Set<User> userEntities = hfe.getUsers();

        List<String> contributors = new ArrayList(userEntities.size());

        for (User user : userEntities) {
            contributors.add(user.getSiteId());
        }

        Collections.sort(contributors);

        hexFrameDTO.setContributors(contributors);

        return hexFrameDTO;

    }

    public List<String> getPayloads(Long satelliteId, Long sequenceNumber, String framesRequested) {
        List<Long> frameList = Stream.of(framesRequested.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        LOG.info(String.format("Payload request %d %d %s", satelliteId, sequenceNumber, framesRequested));

        final List<HexFrame> frames = hexFrameDao.findBySatelliteIdAndSequenceNumberAndFrameTypeInOrderByFrameTypeAsc(
                satelliteId, sequenceNumber, frameList);

        if (frames == null || frames.isEmpty() || frameList.size() != frames.size()) {
            LOG.info("Payloads not found");
            return null;
        }

        List<String> payloads = new ArrayList<>();

        for(HexFrame hexFrame : frames) {
            payloads.add(hexFrame.getPayload().getHexText());
        }

        return payloads;
    }
}
