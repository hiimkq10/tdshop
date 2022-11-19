package com.hcmute.tdshop.utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class Helper {
  public static final String JWT_SECRET = "secretttttt";
  public static final Long JWT_ACCESS_TOKEN_EXPIRATION = 600000L; // 10 minutes in milliseconds
  public static final Long JWT_REFRESH_TOKEN_EXPIRATION = 1800000L; // 30 minutes in milliseconds
  public static final int CONFIRM_TOKEN_DURATION = 5;
  public static final int RESET_PASSWORD_DURATION = 5;
  public static final Algorithm JWT_ALGORITHM = Algorithm.HMAC256(JWT_SECRET.getBytes());
  public static final String dateTimePattern = "yyyy-MM-dd HH:mm";
  public static final String datePattern = "yyyy-MM-dd";
  public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);

  @Autowired
  private UserRepository userRepository;

  public static LocalDateTime MyLocalDateTimeParser(String dateTime) {
    if (dateTime == null) {
      return null;
    }
    return LocalDateTime.parse(dateTime, dateTimeFormatter);
  }

  public static String LocalDateTimeToString(LocalDateTime localDateTime) {
    return localDateTime.format(dateTimeFormatter);
  }

  public static String generateMethodArgumentNotValidExceptionMessage(MethodArgumentNotValidException exception) {
    String errors = "";
    String fieldName = "";
    String errorMessage = "";
    for (ObjectError error : exception.getBindingResult().getAllErrors()) {
      fieldName = ((FieldError) error).getField();
      errorMessage = error.getDefaultMessage();
      errors += fieldName + ": " + errorMessage + "\n";
    }
    return errors;
  }

  public static String generateFileName(String orginalFileName, int offset) {
    LocalDateTime now = LocalDateTime.now();
    String dateTimePattern = "yyyyMMddHHmmss";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
    String[] arrOfOriginalFileName = orginalFileName.split("\\.");
    String fileName = arrOfOriginalFileName[0];
    String fileSuffix = arrOfOriginalFileName[1];
    return fileName + "_" + now.format(dateTimeFormatter) + "_" + String.valueOf(offset) + "." + fileSuffix;
  }

//  public static String findImagePublicId(String url) {
//    return url.substring(url.indexOf(ApplicationConstants.IMAGE_DIRECTORY));
//  }

  public boolean checkIfUsernameExisted(String username) {
    return userRepository.countByUsername(username) > 0;
  }

  public boolean checkIfEmailExisted(String email) {
    return userRepository.countByEmail(email) > 0;
  }

  public boolean checkIfPhoneExisted(String phone) {
    return userRepository.countByPhone(phone) > 0;
  }

  public boolean checkIfStringIsBlank(String str) {
    return str == null || str.isEmpty();
  }
}
