package com.hcmute.tdshop.utils.validators;

import com.hcmute.tdshop.utils.annotations.ValueOfEnum;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

	private List<String> enumValues;

	@Override
	public void initialize(ValueOfEnum constraintAnnotation) {
		enumValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
				.map(Enum::name)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return s != null && enumValues.contains(s);
	}
}
