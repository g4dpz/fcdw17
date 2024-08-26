package uk.me.g4dpz.fcdwrawdata.service.impl;

import org.springframework.http.ResponseEntity;

public class PacketResponseEntity {

    private long satelliteId = 0;
    private long sequenceNumber = 0;
    private long frameType = 0;
    private ResponseEntity responseEntity;

    public PacketResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    public long getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(long satelliteId) {
        this.satelliteId = satelliteId;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getFrameType() {
        return frameType;
    }

    public void setFrameType(long frameType) {
        this.frameType = frameType;
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }
}
