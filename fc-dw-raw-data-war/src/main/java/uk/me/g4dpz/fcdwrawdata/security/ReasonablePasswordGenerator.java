// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public final class ReasonablePasswordGenerator implements PasswordGenerator {

	private static final int PASSWORD_LENGTH = 10;

	@Override
	public String generatePassword() {
		return RandomStringUtils.random(PASSWORD_LENGTH, true, true);
	}

	/**
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public String hashPassword(final String password) throws NoSuchAlgorithmException {
		final MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(password.getBytes());
		final BigInteger hashValue = new BigInteger(1, md5.digest());
		return hashValue.toString(16);
	}

	@Override
	public boolean passwordMatches(final String hash, final String candidate) {
		try {
			return hash.equals(hashPassword(candidate));
		} catch (final Exception e) {
			return false;
		}
	}
}
