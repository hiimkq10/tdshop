package com.hcmute.tdshop.utils.constants;

public class ApplicationConstants {

  public static final String EMAIL_PATTERN =
      "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
  public static final String SUCCESSFUL = "SUCCESSFUL";
  public static final int SUCCESSFUL_CODE = 200;
  public static final String FAILED = "FAILED";
  public static final int FAILED_CODE = 400;
  public static final String UNAUTHORIZED = "UNAUTHORIZED";
  public static final int UNAUTHORIZED_CODE = 401;
  public static final String FORBIDDEN = "FORBIDDEN";
  public static final int FORBIDDEN_CODE = 403;
  public static final String BAD_REQUEST = "BAD_REQUEST";
  public static final int BAD_REQUEST_CODE = 400;
  public static final String BAD_REQUEST_MESSAGE = "Bad request";
  public static final String NOT_FOUND = "NOT_FOUND";
  public static final int NOT_FOUND_CODE = 400;
  public static final String IS_DUPLICATED = "IS_DUPLICATED";
  public static final int DUPLICATED_CODE = 400;
  public static final String CREATED = "CREATED";
  public static final int CREATED_CODE = 201;

  public static final String EMAIL_EXISTED = "Email existed";
  public static final String PHONE_EXISTED = "Phone existed";
  public static final String USERNAME_EXISTED = "Username existed";

  // ------------------------------ USER -------------------------------------
  public static final String USER_NOT_FOUND = "User not found";
  public static final String USER_FIRST_NAME_MANDATORY = "First name is mandatory";
  public static final String USER_FIRST_NAME_SIZE_INVALID = "First name is too long";
  public static final String USER_LAST_NAME_MANDATORY = "Last name is mandatory";
  public static final String USER_LAST_NAME_SIZE_INVALID = "Last name is too long";
  public static final String USER_EMAIL_MANDATORY = "Email is mandatory";
  public static final String USER_EMAIL_SIZE_INVALID = "Email is too long";
  public static final String USER_EMAIL_FORMAT_INVALID = "Email format is invalid";
  public static final String USER_PHONE_MANDATORY = "Phone is mandatory";
  public static final String USER_PHONE_SIZE_INVALID = "Phone is too long";
  public static final String USER_USERNAME_MANDATORY = "Username is mandatory";
  public static final String USER_USERNAME_SIZE_INVALID = "Userame is too long";
  public static final String USER_PASSWORD_MANDATORY = "Password is mandatory";
  public static final String USER_PASSWORD_SIZE_INVALID = "Password is too long";

  // ------------------------------ PRODUCT -------------------------------------
  public static final int PRODUCT_PAGE_SIZE = 5;

  // ------------------------------ JWT -------------------------------------
  public static final String JWT_TOKEN_MISSING = "Jwt token is missing";
  public static final String JWT_CLAIM_ID = "id";
  public static final String JWT_CLAIM_FIRST_NAME = "fname";
  public static final String JWT_CLAIM_LAST_NAME = "lname";

  // ------------------------------ SECURITY -------------------------------------
  public static final String USERNAME_OR_PASSWORD_INCORRECT = "Login failed: username or password is incorrect";
  public static final String USERNAME_OR_PASSWORD_MISSING = "Login failed: username or password is missing";
  public static final String ACCOUNT_INACTIVE = "Login failed: your account has not been activated";
  public static final String UNEXPECTED_ERROR = "Something wrong happened please try again later!";

  // ------------------------------ EMAIL -------------------------------------
  public static final String VERIFICATION_EMAIL_SUBJECT = "Verification for reset password";
}
