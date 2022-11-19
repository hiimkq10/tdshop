package com.hcmute.tdshop.utils.validators;

import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.annotations.ValueOfLocalDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfLocalDateTimeValidator implements ConstraintValidator<ValueOfLocalDateTime, String> {

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    if (s != null) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Helper.dateTimePattern);
      try {
        LocalDateTime.parse(s, dateTimeFormatter);
      }
      catch (DateTimeParseException e) {
        return false;
      }
    }
    return true;
  }
}
