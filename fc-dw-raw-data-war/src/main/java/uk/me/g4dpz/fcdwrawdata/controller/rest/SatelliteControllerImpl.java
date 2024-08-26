package uk.me.g4dpz.fcdwrawdata.controller.rest;

import uk.me.g4dpz.fcdwcommon.controller.SatelliteController;
import uk.me.g4dpz.fcdwcommon.dto.HexFrameDTO;
import uk.me.g4dpz.fcdwrawdata.messaging.JmsMessageSender;
import uk.me.g4dpz.fcdwrawdata.service.HexFrameService;
import uk.me.g4dpz.fcdwrawdata.service.UserRankingService;
import uk.me.g4dpz.fcdwrawdata.service.impl.PacketResponseEntity;
import uk.me.g4dpz.fcdwrawdata.shared.Ranking;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.Collator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class SatelliteControllerImpl implements SatelliteController {

    private static final Logger LOG = LoggerFactory.getLogger(SatelliteControllerImpl.class);

    private final Lock lock = new ReentrantLock();

    private final HexFrameService hexFrameService;
    private UserRankingService userRankingService;

    @Autowired
    JmsMessageSender jmsMessageSender;

    @Autowired
    public SatelliteControllerImpl(HexFrameService hexFrameService, UserRankingService userRankingService) {
        this.hexFrameService = hexFrameService;
        this.userRankingService = userRankingService;
    }

    @PostMapping(value = "/api/data/hex/{site_id}")
    public ResponseEntity processFrame(@PathVariable(value = "site_id") String siteId,
                                       @RequestParam(value = "digest") String digest,
                                       @RequestBody String body) {

        LOG.info("Raw hex frame received from: " + siteId);

        lock.lock();

        try {
            final PacketResponseEntity packetResponseEntity = hexFrameService.processHexFrame(siteId, digest, body);

            final ResponseEntity responseEntity = packetResponseEntity.getResponseEntity();

            final long satelliteId = packetResponseEntity.getSatelliteId();
            final long sequenceNumber = packetResponseEntity.getSequenceNumber();
            final long frameType = packetResponseEntity.getFrameType();

            if (responseEntity.getStatusCode() == HttpStatus.OK
                && !(satelliteId == 0 && sequenceNumber == 0 && frameType == 0)) {

                String queueName = "satellite_" + satelliteId + "_frame_available";

                ActiveMQQueue queue = new ActiveMQQueue(queueName);
                jmsMessageSender.send(queue, String.format("rt,%d,%d,%d",
                        satelliteId,
                        sequenceNumber,
                        frameType));

            }

            return responseEntity;
        }
        finally {
            lock.unlock();
        }
    }

    @GetMapping(value = "/api/data/frame/{satelliteId}/{sequenceNumber}/{frameType}")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HexFrameDTO getHexFrame(
            @PathVariable(value = "satelliteId") final String satelliteId,
            @PathVariable(value = "sequenceNumber") final String sequenceNumber,
            @PathVariable(value = "frameType") final String frameType,
            HttpServletRequest request, HttpServletResponse response) {

        long now = System.currentTimeMillis();

        final long seqNo = Long.parseLong(sequenceNumber);
        final long fType = Long.parseLong(frameType);
        HexFrameDTO frame = hexFrameService.getFrame(Long.parseLong(satelliteId), seqNo, fType);

        if (frame != null) {

            // get the previous frame
            HexFrameDTO previousFrame;
            long previousSeqNo = 0;
            long previousFrameType = 0;

            if (fType == 0) {
                previousSeqNo = seqNo -1;
                previousFrameType = 23;
            }
            else {
                previousSeqNo = seqNo;
                previousFrameType = fType - 1;
            }

            previousFrame = hexFrameService.getFrame(Long.parseLong(satelliteId), previousSeqNo, previousFrameType);

            if (previousFrame != null) {
                final List<String> contributors = previousFrame.getContributors();
                for (String contributor : contributors) {
                    final List<String> contributors1 = frame.getContributors();
                    if (!contributors1.contains(contributor)) {
                        contributors1.add(contributor);
                        java.util.Collections.sort(contributors1, Collator.getInstance());
                        frame.setContributors(contributors1);
                    }
                }
            }

            // and one more
            if (previousFrameType == 0) {
                previousSeqNo = previousSeqNo -1;
                previousFrameType = 23;
            }
            else {
                previousFrameType = previousFrameType - 1;
            }

            previousFrame = hexFrameService.getFrame(Long.parseLong(satelliteId), previousSeqNo, previousFrameType);

            if (previousFrame != null) {
                final List<String> contributors = previousFrame.getContributors();
                for (String contributor : contributors) {
                    final List<String> contributors1 = frame.getContributors();
                    if (!contributors1.contains(contributor)) {
                        contributors1.add(contributor);
                        java.util.Collections.sort(contributors1, Collator.getInstance());
                        frame.setContributors(contributors1);
                    }
                }
            }

            // and one more
            if (previousFrameType == 0) {
                previousSeqNo = previousSeqNo -1;
                previousFrameType = 23;
            }
            else {
                previousFrameType = previousFrameType - 1;
            }

            previousFrame = hexFrameService.getFrame(Long.parseLong(satelliteId), previousSeqNo, previousFrameType);

            if (previousFrame != null) {
                final List<String> contributors = previousFrame.getContributors();
                for (String contributor : contributors) {
                    final List<String> contributors1 = frame.getContributors();
                    if (!contributors1.contains(contributor)) {
                        contributors1.add(contributor);
                        java.util.Collections.sort(contributors1, Collator.getInstance());
                        frame.setContributors(contributors1);
                    }
                }
            }

            frame.setSequenceNumber(seqNo);
            frame.setFrameType(fType);
            LOG.debug(String.format("Time to create realtime info: %d s", (System.currentTimeMillis() - now) / 1000));
            return frame;
        }
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @PostMapping(value = "/api/data/ranking")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Ranking getRanking(HttpServletRequest request) {
        int draw = Integer.parseInt(request.getParameter("draw"));
        int sort = Integer.parseInt(request.getParameter("order[0][column]"));
        int start = Integer.parseInt(request.getParameter("start"));
        int length = Integer.parseInt(request.getParameter("length"));
        String search = request.getParameter("search[value]");

        sort *= request.getParameter("order[0][dir]").equals("asc") ? 1 : -1;

        Ranking ranking = userRankingService.getRanking(draw, sort, start, length, search);

        return ranking;
    }

    @GetMapping(value = "/api/data/payload/{satelliteId}/{sequenceNumber}")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<String> getPayloads(@PathVariable(value = "satelliteId") Long satelliteId,
                                    @PathVariable(value = "sequenceNumber") Long sequenceNumber,
                                    @RequestParam(value = "frames") String frames,
                                    HttpServletRequest request, HttpServletResponse response) {

        final List<String> payloads = hexFrameService.getPayloads(satelliteId, sequenceNumber, frames);

        if (payloads == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        else {
            return payloads;
        }
    }

}
