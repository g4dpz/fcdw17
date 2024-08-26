// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import jakarta.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hex_frame")
public class HexFrame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "hex_frame_user",
            joinColumns = @JoinColumn(name = "hex_frame_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @Column(name = "satellite_id")
    private Long satelliteId;

    @Column(name = "sequence_number")
    private Long sequenceNumber;

    @Column(name = "hex_string", length = 112)
    private String hexString;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "valid")
    private Boolean valid = false;

    @Column(name = "frame_type")
    private Long frameType;

    @Column(name = "wod_processed")
    private Boolean wodProcessed = false;

    @Column(name = "fitter_processed")
    private Boolean fitterProcessed = false;

    @Column(name = "high_precision_processed")
    private Boolean highPrecisionProcessed = false;

    @Column(name = "realtime_processed")
    private Boolean realtimeProcessed = false;

    @Column(name = "satellite_time")
    private Timestamp satelliteTime;

    private Boolean eclipsed = false;

    @Column(name = "eclipse_depth")
    private String eclipseDepth;

    private String latitude;

    private String longitude;

    @Column(name = "out_of_order")
    private Boolean outOfOrder = false;

    @ManyToOne
    @JoinColumn(name="payload_id")
    private Payload payload;

    private String digest;

    @Column(name = "md5_hash")
    private String md5Hash;

    public HexFrame() {
    }

    public HexFrame(
            final Long satelliteId, final Long frameType, final Long sequenceNumber,
            final String hexString, final Date createdDate, final Boolean valid,
            final Timestamp satelliteTime) {
        this.satelliteId = satelliteId;
        this.frameType = frameType;
        this.sequenceNumber = sequenceNumber;
        this.hexString = hexString;
        this.createdDate = createdDate;
        this.valid = valid;
        this.satelliteTime = satelliteTime;
        this.outOfOrder = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> userEntities) {
        this.users = userEntities;
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

    public String getHexString() {
        return hexString;
    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Long getFrameType() {
        return frameType;
    }

    public void setFrameType(Long frameType) {
        this.frameType = frameType;
    }

    public Boolean isWodProcessed() {
        return wodProcessed;
    }

    public void setWodProcessed(Boolean wodProcessed) {
        this.wodProcessed = wodProcessed;
    }

    public Boolean isFitterProcessed() {
        return fitterProcessed;
    }

    public void setFitterProcessed(Boolean fitterProcessed) {
        this.fitterProcessed = fitterProcessed;
    }

    public Boolean isHighPrecisionProcessed() {
        return highPrecisionProcessed;
    }

    public void setHighPrecisionProcessed(Boolean highPrecisionProcessed) {
        this.highPrecisionProcessed = highPrecisionProcessed;
    }

    public Timestamp getSatelliteTime() {
        return satelliteTime;
    }

    public void setSatelliteTime(Timestamp satelliteTime) {
        this.satelliteTime = satelliteTime;
    }

    public Boolean getEclipsed() {
        return eclipsed;
    }

    public void setEclipsed(Boolean eclipsed) {
        this.eclipsed = eclipsed;
    }

    public String getEclipseDepth() {
        return eclipseDepth;
    }

    public void setEclipseDepth(String eclipseDepth) {
        this.eclipseDepth = eclipseDepth;
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

    public Boolean isOutOfOrder() {
        return outOfOrder;
    }

    public void setOutOfOrder(Boolean outOfOrder) {
        this.outOfOrder = outOfOrder;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(final String digest) {
        this.digest = digest;
    }

    public Boolean isRealtimeProcessed() {
        return realtimeProcessed;
    }

    public void setRealtimeProcessed(Boolean realtimeProcessed) {
        this.realtimeProcessed = realtimeProcessed;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
        if (satelliteId == 19 && (frameType == 32 || frameType == 33)) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md5Hash = toHexString(md.digest((hexString + payload.getHexText()).getBytes()));
            } catch (NoSuchAlgorithmException e) {
            }
        }
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
