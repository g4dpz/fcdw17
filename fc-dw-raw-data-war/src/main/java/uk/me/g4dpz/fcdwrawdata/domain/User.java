// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String latitude;
    @Column
    private String longitude;
    @Column(name = "site_id")
    private String siteId;
    @Column
    private boolean enabled;
    @Column
    private boolean admin;
    @Column
    private boolean expired;
    @Column
    private boolean locked;
    @Column(name = "credentials_expired")
    private boolean credentialsExpired;
    @Column(name = "auth_key")
    private String authKey;
    @Column(name = "email_sent")
    private boolean emailSent;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "registration_code")
    private String registrationCode;
    @Column
    private boolean transferred;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<HexFrame> hexFrames;

    public User() {
        hexFrames = new HashSet<HexFrame>();
    }

    public User(final String username, final String password, final String latitude, final String longitude, final String siteId,
                final boolean enabled, final boolean admin, final boolean expired, final boolean locked, final boolean credentialsExpired,
                final String authKey, final boolean emailSent, final Date createdDate, final String registrationCode,
                final boolean transferred) {
        this.username = username;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.siteId = siteId;
        this.enabled = enabled;
        this.admin = admin;
        this.expired = expired;
        this.locked = locked;
        this.credentialsExpired = credentialsExpired;
        this.authKey = authKey;
        this.createdDate = createdDate;
        this.registrationCode = registrationCode;
        this.setEmailSent(emailSent);
        this.transferred = transferred;

        hexFrames = new HashSet<HexFrame>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSiteId() {
        return siteId;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setPassword(final String hashPassword) {
        this.password = hashPassword;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (isAdmin()) {
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authList;

    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    public void setEnabled(final boolean state) {
        this.enabled = state;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public void addFrame(final HexFrame hexFrame) {
        if (!getFrames().contains(hexFrame)) {
            getFrames().add(hexFrame);
        }
        if (!hexFrame.getUsers().contains(this)) {
            hexFrame.getUsers().add(this);
        }
    }

    public Set<HexFrame> getFrames() {
        return hexFrames;
    }

    public void setFrames(final Set<HexFrame> frames) {
        this.hexFrames = frames;
    }

    public boolean isTransferred() {
        return transferred;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
