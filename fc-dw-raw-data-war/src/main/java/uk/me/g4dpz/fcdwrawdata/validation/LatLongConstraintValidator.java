// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LatLongConstraintValidator implements ConstraintValidator<LatLong, String> {

	@Override
	public void initialize(LatLong latLong) {
	}

	@Override
	public boolean isValid(String latLongField, ConstraintValidatorContext cxt) {
		if (latLongField == null) {
			return false;
		}
		return latLongField.matches("^[+-]?[0-9]{1,3}(?:\\.[0-9]{1,4})?$");
	}
}
