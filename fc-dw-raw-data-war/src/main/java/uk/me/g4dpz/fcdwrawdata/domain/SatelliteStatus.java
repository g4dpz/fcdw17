package uk.me.g4dpz.fcdwrawdata.domain;

/**
 * Created by davidjohnson on 25/09/2016.
 */
import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "satellite_status")
public class SatelliteStatus {

    @Id
    @Column(name = "satellite_id")
    private Long satelliteId;

    @Column(name = "sequence_number")
    private Long sequenceNumber;
    @Column(name = "eclipse_mode_forced")
    private Boolean eclipseModeForced;
    private Boolean eclipsed;
    @Column(name = "last_updated")
    private Date lastUpdated;
    @Column(name = "eclipse_depth")
    private Double eclipseDepth;
    @Column(name = "eclipse_switch")
    private Boolean eclipseSwitch;
    @Column(name = "last_wod_dump")
    private Date lastWodDump;
    @Column(name = "last_reset_notification")
    private Date lastResetNotification;
    @Column(name = "last_no_show_notification")
    private Date lastNoShowNotification;
    @Column(name = "epoch_sequence_number")
    private Long epochSequenceNumber;
    @Column(name = "epochreference_time")
    private Date epochReferenceTime;
    @Column(name = "packet_count")
    private Long packetCount;
    private String latitude;
    private String longitude;
    private String contributors;
    @Column(name = "last_wod_time")
    private Date lastWodTime;
    @Column(name = "last_highres_time")
    private Date lastHighresTime;
    @Column(name = "last_realime_time")
    private Date lastRealtimeTime;
    @Column(name = "last_fitter_time")
    private Date lastFitterTime;
    @Column(name = "frame_type")
    private Long frameType;
    @Column(name = "catalogue_number")
    private Long catalogueNumber;

    public SatelliteStatus() {
    }

    public Long getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(Long satelliteId) {
        this.satelliteId = satelliteId;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Boolean isEclipseModeForced() {
        return eclipseModeForced;
    }

    public void setEclipseModeForced(Boolean eclipseModeForced) {
        this.eclipseModeForced = eclipseModeForced;
    }

    public Boolean isEclipsed() {
        return eclipsed;
    }

    public void setEclipsed(Boolean eclipsed) {
        this.eclipsed = eclipsed;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getEclipseDepth() {
        return eclipseDepth;
    }

    public void setEclipseDepth(Double eclipseDepth) {
        this.eclipseDepth = eclipseDepth;
    }

    public void setEclipseSwitch(Boolean eclipseSwitch) {
        this.eclipseSwitch = eclipseSwitch;
    }

    public Boolean getEclipseModeForced() {
        return eclipseModeForced;
    }

    public Boolean getEclipsed() {
        return eclipsed;
    }

    public Boolean isEclipseSwitch() {
        return eclipseSwitch;
    }

    public Long getEpochSequenceNumber() {
        return epochSequenceNumber;
    }

    public void setEpochSequenceNumber(Long epochSequenceNumber) {
        this.epochSequenceNumber = epochSequenceNumber;
    }

    public Date getEpochReferenceTime() {
        return epochReferenceTime;
    }

    public void setEpochReferenceTime(Date epochReferenceTime) {
        this.epochReferenceTime = epochReferenceTime;
    }

    @Transient
    public String getMode() {
        if (isEclipseSwitch()) {
            return "Auto";
        }
        else {
            return "Manual";
        }
    }

    @Transient
    public String getTransponderState() {
        if (!isEclipseSwitch()) {
            if (!isEclipseModeForced()) {
                return "Off";
            }
            else {
                return "On";
            }
        }
        else {
            if (!isEclipseModeForced()) {
                if (!isEclipsed()) {
                    return "Off";
                }
                else {
                    return "On";
                }
            }
            else {
                if (!isEclipsed()) {
                    return "Off";
                }
                else {
                    return "On";
                }
            }
        }
    }

    public Date getLastWodDump() {
        return lastWodDump;
    }

    public void setLastWodDump(Date lastWodDump) {
        this.lastWodDump = lastWodDump;
    }

    public Date getLastResetNotification() {
        return lastResetNotification;
    }

    public void setLastResetNotification(Date lastResetNotification) {
        this.lastResetNotification = lastResetNotification;
    }

    public Date getLastNoShowNotification() {
        return lastNoShowNotification;
    }

    public void setLastNoShowNotification(Date lastNoShowNotification) {
        this.lastNoShowNotification = lastNoShowNotification;
    }

    public Long getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(Long packetCount) {
        this.packetCount = packetCount;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public Date getLastWodTime() {
        return lastWodTime;
    }

    public void setLastWodTime(Date lastWodTime) {
        this.lastWodTime = lastWodTime;
    }

    public Date getLastHighresTime() {
        return lastHighresTime;
    }

    public void setLastHighresTime(Date lastHighresTime) {
        this.lastHighresTime = lastHighresTime;
    }

    public Date getLastRealtimeTime() {
        return lastRealtimeTime;
    }

    public void setLastRealtimeTime(Date lastRealtimeTime) {
        this.lastRealtimeTime = lastRealtimeTime;
    }

    public Date getLastFitterTime() {
        return lastFitterTime;
    }

    public void setLastFitterTime(Date lastFitterTime) {
        this.lastFitterTime = lastFitterTime;
    }

    public Long getFrameType() {
        return frameType;
    }

    public void setFrameType(Long frameType) {
        this.frameType = frameType;
    }

    public Long getCatalogueNumber() {
        return catalogueNumber;
    }

    public void setCatalogueNumber(Long catelogueNumber) {
        this.catalogueNumber = catelogueNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SatelliteStatus)) return false;
        SatelliteStatus that = (SatelliteStatus) o;
        return getSatelliteId().equals(that.getSatelliteId()) &&
                getSequenceNumber().equals(that.getSequenceNumber()) &&
                getEclipseModeForced().equals(that.getEclipseModeForced()) &&
                getEclipsed().equals(that.getEclipsed()) &&
                getLastUpdated().equals(that.getLastUpdated()) &&
                getEclipseDepth().equals(that.getEclipseDepth()) &&
                eclipseSwitch.equals(that.eclipseSwitch) &&
                getLastWodDump().equals(that.getLastWodDump()) &&
                getLastResetNotification().equals(that.getLastResetNotification()) &&
                getLastNoShowNotification().equals(that.getLastNoShowNotification()) &&
                getEpochSequenceNumber().equals(that.getEpochSequenceNumber()) &&
                getEpochReferenceTime().equals(that.getEpochReferenceTime()) &&
                getPacketCount().equals(that.getPacketCount()) &&
                getLatitude().equals(that.getLatitude()) &&
                getLongitude().equals(that.getLongitude()) &&
                getContributors().equals(that.getContributors()) &&
                getLastWodTime().equals(that.getLastWodTime()) &&
                getLastHighresTime().equals(that.getLastHighresTime()) &&
                getLastRealtimeTime().equals(that.getLastRealtimeTime()) &&
                getLastFitterTime().equals(that.getLastFitterTime()) &&
                getFrameType().equals(that.getFrameType()) &&
                getCatalogueNumber().equals(that.getCatalogueNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSatelliteId(), getSequenceNumber(), getEclipseModeForced(), getEclipsed(), getLastUpdated(), getEclipseDepth(), eclipseSwitch, getLastWodDump(), getLastResetNotification(), getLastNoShowNotification(), getEpochSequenceNumber(), getEpochReferenceTime(), getPacketCount(), getLatitude(), getLongitude(), getContributors(), getLastWodTime(), getLastHighresTime(), getLastRealtimeTime(), getLastFitterTime(), getFrameType(), getCatalogueNumber());
    }

    @Override
    public String toString() {
        return "SatelliteStatus{" +
                "satelliteId=" + satelliteId +
                ", sequenceNumber=" + sequenceNumber +
                ", eclipseModeForced=" + eclipseModeForced +
                ", eclipsed=" + eclipsed +
                ", lastUpdated=" + lastUpdated +
                ", eclipseDepth=" + eclipseDepth +
                ", eclipseSwitch=" + eclipseSwitch +
                ", lastWodDump=" + lastWodDump +
                ", lastResetNotification=" + lastResetNotification +
                ", lastNoShowNotification=" + lastNoShowNotification +
                ", epochSequenceNumber=" + epochSequenceNumber +
                ", epochReferenceTime=" + epochReferenceTime +
                ", packetCount=" + packetCount +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", contributors='" + contributors + '\'' +
                ", lastWodTime=" + lastWodTime +
                ", lastHighresTime=" + lastHighresTime +
                ", lastRealtimeTime=" + lastRealtimeTime +
                ", lastFitterTime=" + lastFitterTime +
                ", frameType=" + frameType +
                ", catelogueNumber=" + catalogueNumber +
                '}';
    }
}
