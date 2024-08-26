// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public final class BcryptPasswordGenerator implements PasswordGenerator {

	private static final int PASSWORD_LENGTH = 10;

	@Override
	public String generatePassword() {
		return RandomStringUtils.random(PASSWORD_LENGTH, true, true);
	}

	/**
	 * @See http://codahale.com/how-to-safely-store-a-password/
	 */
	@Override
	public String hashPassword(final String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());

	}

	@Override
	public boolean passwordMatches(final String hash, final String candidate) {
		return BCrypt.checkpw(candidate, hash);
	}
}
