package uk.me.g4dpz.datawarehouse.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HexFrameDTO implements Serializable {
    private Long satelliteId;
    private Long sequenceNumber;
    private Long frameType;
    private String hexString = "";
    private Date createdDate;
    private String latitude;
    private String longitude;
    private Date satelliteTime;
    private List<String> contributors = new ArrayList();

    public HexFrameDTO() {
    }

    public Long getSatelliteId() {
        return this.satelliteId;
    }

    public Long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public Long getFrameType() {
        return this.frameType;
    }

    public String getHexString() {
        return this.hexString;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setSatelliteId(Long satelliteId) {
        this.satelliteId = satelliteId;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setFrameType(Long frameType) {
        this.frameType = frameType;
    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getSatelliteTime() {
        return this.satelliteTime;
    }

    public void setSatelliteTime(Date satelliteTime) {
        this.satelliteTime = satelliteTime;
    }

    public List<String> getContributors() {
        return this.contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }
}
