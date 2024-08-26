package uk.me.g4dpz.fcdwrawdata.messaging;

import uk.me.g4dpz.fcdwrawdata.dao.HexFrameDao;
import uk.me.g4dpz.fcdwrawdata.domain.HexFrame;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Receiver {

    @Autowired
    HexFrameDao hexFrameDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @JmsListener(destination = "frame_processed")
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public void receive(String message) {
        LOGGER.info("received message='{}'", message);


        if (message.indexOf("Error") == 0) {
            LOGGER.error(String.format("Service returned error: " + message));
            return;
        }

        String[] messageElements = StringUtils.split(message, ",");
        int msgLength = messageElements.length;
        if (msgLength < 3 || msgLength > 4) {
            LOGGER.warn(String.format("Frame to be processed was incorrect: " + message));
            return;
        }

        String messageType = messageElements[0];
        long satelliteId = Long.parseLong(messageElements[1]);
        long sequenceNumber = Long.parseLong(messageElements[2]);
        boolean success = false;

        if (messageType.equals("rt")) {
            long frameType = Long.parseLong(messageElements[3]);
            LOGGER.debug("Updating realtime " + message + " as processed" );
            List<HexFrame> hexFrameEntities = hexFrameDao.findBySatelliteIdAndSequenceNumberAndFrameType(satelliteId, sequenceNumber, frameType);
            if ((hexFrameEntities != null) && (hexFrameEntities.size() == 1)) {
                HexFrame hfe = hexFrameEntities.get(0);
                hfe.setRealtimeProcessed(true);
                hexFrameDao.save(hfe);

                success = true;
            }
            else {
                LOGGER.error("Failed to find " + message + " to update realtime as processed" );
            }
        }

        if (!success) {
            LOGGER.error("Could not process " + message);
        }
        latch.countDown();
    }
}
