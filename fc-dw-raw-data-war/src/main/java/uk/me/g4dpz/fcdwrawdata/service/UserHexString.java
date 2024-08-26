// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.service;

import uk.me.g4dpz.fcdwrawdata.domain.User;

import java.util.Date;

/**
 * @author g4dpz
 *
 */
public class UserHexString {

    private final User user;
    private final String hexString;
    private final Date createdDate;

    public UserHexString(final User user, final String hexString,
                         final Date createdDate) {
        this.user = user;
        this.hexString = hexString;
        this.createdDate = createdDate;
    }

    /**
     * @return the createdDate
     */
    public final Date getCreatedDate() {
        return createdDate;
    }

    public final String getHexString() {
        return hexString;
    }

    public final User getUser() {
        return user;
    }

}
