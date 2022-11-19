package com.hcmute.tdshop.utils.validators;

import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.annotations.ValueOfLocalDate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfLocalDateValidator implements ConstraintValidator<ValueOfLocalDate, String> {

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    if (s != null) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Helper.dateTimePattern);
      try {
        LocalDate.parse(s, dateTimeFormatter);
      } catch (DateTimeParseException e) {
        return false;
      }
    }
    return true;
  }
}
