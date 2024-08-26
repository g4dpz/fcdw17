// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.validation;

import uk.me.g4dpz.fcdwrawdata.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailNotDuplicateConstraintValidator implements ConstraintValidator<EmailNotDuplicate, String> {

	@Autowired
	UserDao userDao;

	@Override
	public void initialize(EmailNotDuplicate emailNotDuplicate) {
	}

	@Transactional(readOnly = true)
	@Override
	public boolean isValid(String emailNotDuplicateField, ConstraintValidatorContext cxt) {
		if (emailNotDuplicateField == null) {
			return false;
		}
		return (userDao.findByUsername(emailNotDuplicateField) == null);
	}
}
