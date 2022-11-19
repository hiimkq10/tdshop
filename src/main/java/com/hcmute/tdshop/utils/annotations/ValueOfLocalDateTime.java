package com.hcmute.tdshop.utils.annotations;

import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import com.hcmute.tdshop.utils.validators.ValueOfLocalDateTimeValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ValueOfLocalDateTimeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueOfLocalDateTime {
  String message() default ApplicationConstants.LOCAL_DATE_TIME_FORMAT_INVALID;
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
