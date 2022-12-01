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
  public static final String LOCAL_DATE_TIME_FORMAT_INVALID = "Date time format is invalid";
  public static final String LOCAL_DATE_FORMAT_INVALID = "Date format is invalid";

  // ------------------------------ AUTH -------------------------------------
  public static final String CURRENT_PASSWORD_MISSING = "Current password is missing";
  public static final String CURRENT_PASSWORD_MAX_SIZE_INVALID = "Current password is too long";
  public static final String CURRENT_PASSWORD_WRONG = "Current password is wrong";
  public static final String NEW_PASSWORD_MISSING = "New password is missing";
  public static final String NEW_PASSWORD_MAX_SIZE_INVALID = "New password is too long";
  public static final String CONFIRM_PASSWORD_MISSING = "Confirm password is missing";
  public static final String CONFIRM_PASSWORD_NOT_MATCH = "Confirm password does not match new password";
  public static final String CONFIRM_PASSWORD_MAX_SIZE_INVALID = "Confirm password is too long";
  public static final String CHANGE_PASSWORD_SUCCESSFULLY = "Change password successfully";
  public static final String RESET_PASSWORD_SUCCESSFULLY = "Reset password successfully";
  public static final String TOKEN_MANDATORY = "Token is mandatory";
  public static final String TOKEN_INVALID = "Token is invalid";
  public static final String TOKEN_EXPIRED = "Token expired";
  public static final String TOKEN_WRONG = "Token is wrong";
  public static final String TOKEN_NOT_CONFIRMED = "Token not confirmed";
  public static final String TOKEN_USED = "Token has been used";
  public static final String RESET_PASSWORD_DURATION_OVER = "Reset password time is over!!";
  public static final String ACCOUNT_ACTIVATED = "Account activated";

  // ------------------------------ USER -------------------------------------
  public static final String USER_ID_INVALID = "User id is invalid";
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

  // ------------------------------ ADDRESS -------------------------------------
  public static final String ADDRESS_ID_INVALID = "Address id is invalid";
  public static final String ADDRESS_NOT_FOUND = "Address not found";
  public static final String ADDRESS_NAME_MISSING = "Name is missing";
  public static final String ADDRESS_NAME_SIZE_INVALID = "Name is too long";
  public static final String ADDRESS_EMAIL_MANDATORY = "Email is mandatory";
  public static final String ADDRESS_EMAIL_SIZE_INVALID = "Email is too long";
  public static final String ADDRESS_EMAIL_FORMAT_INVALID = "Email format is invalid";
  public static final String ADDRESS_PHONE_MANDATORY = "Phone is mandatory";
  public static final String ADDRESS_PHONE_SIZE_INVALID = "Phone is too long";
  public static final String ADDRESS_DETAIL_MISSING = "Address detail is missing";
  public static final String ADDRESS_DETAIL_SIZE_INVALID = "Address detail is too long";
  public static final String ADDRESS_WARDS_ID_INVALID = "Wards id is invalid";
  public static final String ADDRESS_USER_ID_INVALID = "User id is invalid";
  public static final String ADDRESS_ADD_SUCCESSFULLY = "Add address successfully";
  public static final String ADDRESS_UPDATE_SUCCESSFULLY = "Update address successfully";
  public static final String ADDRESS_DELETE_SUCCESSFULLY = "Delete address successfully";

  // ------------------------------ PROVINCE -------------------------------------
  public static final String PROVINCE_NOT_FOUND = "Province not found";

  // ------------------------------ DISTRICT -------------------------------------
  public static final String DISTRICT_NOT_FOUND = "District not found";

  // ------------------------------ WARDS -------------------------------------
  public static final String WARDS_NOT_FOUND = "Wards not found";

  // ------------------------------ PRODUCT -------------------------------------
  public static final String PRODUCT_NOT_FOUND = "Product not found";
  public static final String PRODUCT_ID_INVALID = "Product id is invalid";
  public static final String PRODUCT_QUANTITY_MUST_BIGGER_THAN_0 = "Product quantity must bigger than 0";
  public static final String PRODUCT_QUANTITY_NOT_ENOUGH = "Sorry, we dont have enough products for your order";

  // ------------------------------ CART -------------------------------------
  public static final String ADD_ITEM_SUCCESSFULLY = "Add item successfully";
  public static final String REMOVE_ITEM_SUCCESSFULLY = "Remove item successfully";
  public static final String CHANGE_ITEM_QUANTITY_SUCCESSFULLY = "Change item quantity successfully";

  // ------------------------------ ORDER -------------------------------------
  public static final String ORDER_PRODUCTS_EMPTY = "You must choose at least 1 product to make order";
  public static final String ORDER_NOT_FOUND = "Order not found";

  // ------------------------------ ORDER STATUS -------------------------------------
  public static final String ORDER_STATUS_NOT_FOUND = "Order status not found";
  public static final String ONLY_AWAITING_PAYMENT_ORDER_CAN_BE_DELETED = "Only awaiting payment order can be deleted";

  // ------------------------------ PAYMENT -------------------------------------
  public static final String PAYMENT_ID_INVALID = "Payment id invalid";
  public static final String PAYMENT_NOT_FOUND = "Payment method not found";

  // ------------------------------ SHIP -------------------------------------
  public static final String SHIP_ID_INVALID = "Ship id invalid";
  public static final String SHIP_NOT_FOUND = "Ship not found";

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
