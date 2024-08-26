package uk.me.g4dpz.fcdwrawdata.domain;

import java.sql.Timestamp;
import java.util.Date;


import jakarta.persistence.*;

@Entity
@Table(name = "user_ranking")
public class UserRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "satellite_id")
    private Long satelliteId;
    @Column(name = "site_id")
    private String siteId;
    @Column
    private Long number;
    @Column(name = "latest_upload_date")
    private Timestamp latestUploadDate;
    @Column(name = "site_alias")
    private String siteAlias;
    @Column(name = "first_upload_date")
    private Timestamp firstUploadDate;

    public UserRanking() {

    }

    public UserRanking(final Long satelliteId, final String siteId, final Long number,
                             final Timestamp latestUploadDate, final Timestamp firstUploadDate) {
        super();
        this.satelliteId = satelliteId;
        this.siteId = siteId;
        this.number = number;
        this.latestUploadDate = latestUploadDate;
        this.firstUploadDate = firstUploadDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSatelliteId(Long satelliteId) {
        this.satelliteId = satelliteId;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public long getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(long satelliteId) {
        this.satelliteId = satelliteId;
    }

    public Date getLatestUploadDate() {
        return latestUploadDate;
    }

    public void setLatestUploadDate(Timestamp latestUploadDate) {
        this.latestUploadDate = latestUploadDate;
    }

    public String getSiteAlias() {
        return siteAlias;
    }

    public void setSiteAlias(String siteAlias) {
        this.siteAlias = siteAlias;
    }

    public Timestamp getFirstUploadDate() {
        return firstUploadDate;
    }

    public void setFirstUploadDate(Timestamp firstUploadDate) {
        this.firstUploadDate = firstUploadDate;
    }

}
