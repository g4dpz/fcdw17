// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwrawdata.validation;

import org.apache.commons.beanutils.BeanUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
	private String firstFieldName;
	private String secondFieldName;
	private String errorMessage;

	@Override
	public void initialize(final FieldMatch constraintAnnotation) {
		firstFieldName = constraintAnnotation.first();
		secondFieldName = constraintAnnotation.second();
		errorMessage = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {

		boolean toReturn = false;

		try {
			final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
			final Object secondObj = BeanUtils.getProperty(value, secondFieldName);

			// System.out.println("firstObj = "+firstObj+"   secondObj = "+secondObj);

			toReturn = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
		} catch (final Exception e) {
			System.out.println(e.toString());
		}
		// If the validation failed
		if (!toReturn) {
			context.disableDefaultConstraintViolation();
			// In the initialiaze method you get the errorMessage: constraintAnnotation.message();
			context.buildConstraintViolationWithTemplate(errorMessage).addNode(firstFieldName).addConstraintViolation();
			context.buildConstraintViolationWithTemplate(errorMessage).addNode(secondFieldName).addConstraintViolation();

		}
		return toReturn;
	}
}
