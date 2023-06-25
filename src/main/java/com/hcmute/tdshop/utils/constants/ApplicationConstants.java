package com.hcmute.tdshop.utils.constants;

public class ApplicationConstants {
//  public static final String AREAS_FILE = "src\\main\\resources\\data\\areas.xlsx";
public static final String AREAS_FILE = "/data/areas.xlsx";
  public static final String AREAS_SHEET_NAME = "Sheet1";
  // BE Endpoint
  public static final String activateAccountEndpoint= "/api/v1/auth/activate";

  // FE Endpoint
  public static final String activateAccountSuccessEndpoint = "/";
  public static final String activateAccountFailEndpoint = "/resend-token";


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
  public static final int PRODUCT_QUANTITY_NOT_ENOUGH_CODE = 20001;

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
  public static final String USER_NOT_LOGIN = "Please log in to use this function";
  public static final String USER_BAN_THEMSELF = "You can not ban your account";
  public static final String USER_UN_BAN_THEMSELF = "You can not unban your account";

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
  public static final String USER_SALARY_INVALID = "Salary is invalid";
  public static final String USER_ADD_SUCCESSFULLY = "Add user successfully";
  public static final String USER_UPDATE_SUCCESSFULLY = "Update user successfully";
  public static final String USER_BAN_SUCCESSFULLY = "Ban user successfully";
  public static final String USER_UN_BAN_SUCCESSFULLY = "Unban user successfully";
  public static final String USER_ADD_FAILED = "Add user failed";
  public static final String USER_ID_OR_ROLE_NOT_FOUND = "User %d doesn`t have id or role";

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
  public static final String PRODUCT_NAME_MANDATORY = "Product name is mandatory";
  public static final String PRODUCT_NAME_SIZE_INVALID = "Product name is too long";
  public static final String PRODUCT_PRICE_INVALID = "Product price is invalid";
  public static final String PRODUCT_DESCRIPTION_MANDATORY = "Product description is mandatory";
  public static final String PRODUCT_DESCRIPTION_SIZE_INVALID = "Product description is too long";
  public static final String PRODUCT_SHORT_DESCRIPTION_MANDATORY = "Product short description is mandatory";
  public static final String PRODUCT_SHORT_DESCRIPTION_SIZE_INVALID = "Product short description is too long";
  public static final String PRODUCT_TOTAL_INVALID = "Product total is invalid";
  public static final String PRODUCT_MUST_BELONGS_TO_ONE_CATEGORY = "Product must at least belongs to one category";
  public static final String PRODUCT_ADD_SUCCESSFULLY = "Add product successfully";
  public static final String PRODUCT_UPDATE_SUCCESSFULLY = "Update product successfully";
  public static final String PRODUCT_DELETE_SUCCESSFULLY = "Delete product successfully";

  public static final String PRODUCT_STATUS_INVALID = "Product status invalid";
  public static final String PRODUCT_STATUS_NOT_FOUND = "Product status not found";

  // ------------------------------ CART -------------------------------------
  public static final String IMAGE_UPLOAD_FAILED = "Failed to save image!! Please check your network and try again";

  public static final String ADD_ITEM_SUCCESSFULLY = "Add item successfully";
  public static final String REMOVE_ITEM_SUCCESSFULLY = "Remove item successfully";
  public static final String CHANGE_ITEM_QUANTITY_SUCCESSFULLY = "Change item quantity successfully";

  // ------------------------------ ORDER -------------------------------------
  public static final String ORDER_PRODUCTS_EMPTY = "You must choose at least 1 product to make order";
  public static final String ORDER_NOT_FOUND = "Order not found";
  public static final int ORDER_NOT_FOUND_CODE = 20007;
  public static final String ORDER_SUCCESSFULLY = "Order successfully";
  public static final String ORDER_CANCEL_SUCCESSFULLY = "Cancel order successfully";

  // ------------------------------ ORDER STATUS -------------------------------------
  public static final String ORDER_STATUS_NOT_FOUND = "Order status not found";
  public static final String ONLY_AWAITING_PAYMENT_ORDER_CAN_BE_CANCELED = "Only awaiting payment order can be canceled";
  public static final int ONLY_AWAITING_PAYMENT_ORDER_CAN_BE_CANCELED_CODE = 20002;

  public static final String ORDER_LALAMOVE_REGION_NOT_SUPPORT = "Only support HN, HCM, ĐN";
  public static final int ORDER_LALAMOVE_REGION_NOT_SUPPORT_CODE = 20003;

  public static final String ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT = "COD amount invalid";
  public static final int ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT_CODE = 20004;

  public static final String ORDER_SIZE_EXCEED_SUPPORT_SIZE = "Size invalid";
  public static final int ORDER_SIZE_EXCEED_SUPPORT_SIZE_CODE = 20005;
  public static final String ORDER_LALAMOVE_COD_NOT_SUPPORT = "Lalamove cod not support";
  public static final int ORDER_LALAMOVE_COD_NOT_SUPPORT_CODE = 20006;
  public static final String ORDER_PAID = "Order paid";
  public static final int ORDER_PAID_CODE = 20008;
  public static final String ORDER_PAYMENT_EXPIRED = "Order payment expired";
  public static final int ORDER_PAYMENT_EXPIRED_CODE = 20009;

  // ------------------------------ PAYMENT -------------------------------------
  public static final String PAYMENT_ID_INVALID = "Payment id invalid";
  public static final String PAYMENT_NOT_FOUND = "Payment method not found";

  // ------------------------------ SHIP -------------------------------------
  public static final String SHIP_ID_INVALID = "Ship id invalid";
  public static final String SHIP_NOT_FOUND = "Ship not found";

  // ------------------------------ BRAND -------------------------------------
  public static final String BRAND_ID_INVALID = "Brand id is invalid";
  public static final String BRAND_NOT_FOUND = "Brand not found";
  public static final String BRAND_NAME_EXISTED = "Brand name existed";
  public static final String BRAND_NAME_MANDATORY = "Brand name is mandatory";
  public static final String BRAND_NAME_SIZE_INVALID = "Brand name is too long";
  public static final String BRAND_PRODUCT_EXIST = "Can`t delete brand!! Please remove all product of this brand before delete";
  public static final String BRAND_ADD_SUCCESSFULLY = "Add brand successfully";
  public static final String BRAND_UPDATE_SUCCESSFULLY = "Update brand successfully";
  public static final String BRAND_DELETE_SUCCESSFULLY = "Delete brand successfully";

  // ------------------------------ MASTER CATEGORY -------------------------------------
  public static final String MASTER_CATEGORY_ID_INVALID = "Master category is invalid";
  public static final String MASTER_CATEGORY_NOT_FOUND = "Master category not found";
  public static final int MASTER_CATEGORY_NOT_FOUND_CODE = 50001;
  public static final String MASTER_CATEGORY_NAME_EXISTED = "Master category name existed";
  public static final String MASTER_CATEGORY_NAME_MANDATORY = "Master category name is mandatory";
  public static final String MASTER_CATEGORY_NAME_SIZE_INVALID = "Master category name is too long";
  public static final String MASTER_CATEGORY_RELATED_EXIST = "Can`t delete master category!! Please remove all category and variation related to this master category before delete";
  public static final String MASTER_CATEGORY_ADD_SUCCESSFULLY = "Add master category successfully";
  public static final String MASTER_CATEGORY_UPDATE_SUCCESSFULLY = "Update master category successfully";
  public static final String MASTER_CATEGORY_DELETE_SUCCESSFULLY = "Delete master category successfully";

  // ------------------------------ CATEGORY -------------------------------------
  public static final String CATEGORY_NOT_FOUND = "Category not found";
  public static final String PARENT_CATEGORY_ID_INVALID = "Parent category is invalid";
  public static final String CATEGORY_NAME_EXISTED = "Category name existed";
  public static final String CATEGORY_NAME_MANDATORY = "Category name is mandatory";
  public static final String CATEGORY_NAME_SIZE_INVALID = "Category name is too long";
  public static final String CATEGORY_RELATED_EXIST = "Can`t delete category!! Please remove all product or child category related to this category before delete";
  public static final String CATEGORY_TWO_LEVEL_ERROR = "You can only have 2 level category";
  public static final String PARENT_MASTER_CATEGORY_SAME_ERROR = "Parent category`s master category must be the same as children";
  public static final String CATEGORY_ADD_SUCCESSFULLY = "Add category successfully";
  public static final String CATEGORY_UPDATE_SUCCESSFULLY = "Update category successfully";
  public static final String CATEGORY_DELETE_SUCCESSFULLY = "Delete category successfully";

  // ------------------------------ VARIATION -------------------------------------
  public static final String VARIATION_NOT_FOUND = "Variation not found";
  public static final int VARIATION_NOT_FOUND_CODE = 60002;
  public static final String VARIATION_NAME_EXISTED = "Variation name existed";
  public static final int VARIATION_NAME_EXISTED_CODE = 60001;
  public static final String VARIATION_NAME_MANDATORY = "Variation name is mandatory";
  public static final String VARIATION_NAME_SIZE_INVALID = "Variation name is too long";
  public static final String VARIATION_ADD_SUCCESSFULLY = "Add variation successfully";
  public static final String VARIATION_UPDATE_SUCCESSFULLY = "Update variation successfully";
  public static final String VARIATION_DELETE_SUCCESSFULLY = "Delete variation successfully";
  public static final String VARIATION_RELATED_EXIST = "Can`t delete variation!! Please remove all variation options related to this category before delete";

  // ------------------------------ VARIATION OPTION -------------------------------------
  public static final String VARIATION_OPTION_NAME_EXISTED = "Variation option name existed";
  public static final int VARIATION_OPTION_NAME_EXISTED_CODE = 70001;
  public static final String VARIATION_OPTION_VALUE_SIZE_INVALID = "Variation option value size invalid";
  public static final int VARIATION_OPTION_VALUE_SIZE_INVALID_CODE = 70002;
  public static final String VARIATION_OPTION_VALUE_MANDATORY = "Variation option value size invalid";
  public static final int VARIATION_OPTION_VALUE_MANDATORY_CODE = 70003;

  // ------------------------------ ATTRIBUTE_SET -------------------------------------
  public static final String ATTRIBUTE_SET_NOT_FOUND = "Attribute set not found";
  public static final String ATTRIBUTE_SET_NAME_EXISTED = "Attribute set name existed";
  public static final int ATTRIBUTE_SET_NAME_EXISTED_CODE = 30001;
  public static final String ATTRIBUTE_SET_NAME_MANDATORY = "Attribute set name is mandatory";
  public static final String ATTRIBUTE_SET_NAME_SIZE_INVALID = "Attribute set name is too long";
  public static final String ATTRIBUTE_SET_ADD_SUCCESSFULLY = "Add variation successfully";
  public static final String ATTRIBUTE_SET_UPDATE_SUCCESSFULLY = "Update variation successfully";
  public static final String ATTRIBUTE_SET_DELETE_SUCCESSFULLY = "Delete variation successfully";
  public static final String ATTRIBUTE_SET_RELATED_EXIST = "Can`t delete attribute set!! Please remove all attribute related to this category before delete";


  // ------------------------------ ATTRIBUTE -------------------------------------
  public static final String ATTRIBUTE_PRIORITY_INVALID = "Attribute priority is invalid";
  public static final String ATTRIBUTE_NAME_EXISTED = "Attribute name existed";
  public static final int ATTRIBUTE_NAME_EXISTED_CODE = 40001;
  public static final String ATTRIBUTE_NAME_MANDATORY = "Attribute name is mandatory";
  public static final String ATTRIBUTE_NAME_SIZE_INVALID = "Attribute name is too long";

  // ------------------------------ PROMOTION -------------------------------------
  public static final String PROMOTION_NOT_FOUND = "Promotion not found";
  public static final String PROMOTION_START_DATE_NOT_BEFORE_END_DATE = "Start date must befor end date";
  public static final String PROMOTION_NAME_MANDATORY = "Promotion name is mandatory";
  public static final String PROMOTION_NAME_SIZE_INVALID = "Promotion name is too long";
  public static final String PROMOTION_DESCRIPTION_MANDATORY = "Promotion description is mandatory";
  public static final String PROMOTION_DESCRIPTION_SIZE_INVALID = "Promotion description is too long";
  public static final String PROMOTION_DISCOUNT_RATE_SMALLER_THAN_0 = "Promotion discount rate must bigger than 0";
  public static final String PROMOTION_ADD_SUCCESSFULLY = "Add promotion successfully";
  public static final String PROMOTION_UPDATE_SUCCESSFULLY = "Update promotion successfully";
  public static final String PROMOTION_DELETE_SUCCESSFULLY = "Delete promotion successfully";

  // ------------------------------ REVIEW -------------------------------------
  public static final String REVIEW_NOT_FOUND= "Review not found";
  public static final String REVIEW_ADD_SUCCESSFULLY = "Add review successfully";
  public static final String REVIEW_DELETE_SUCCESSFULLY = "Delete review successfully";
  public static final String REVIEW_VERIFY_SUCCESSFULLY = "Verify review successfully";

  // ------------------------------ PAYMENT -------------------------------------
  public static final String PAYMENT_SIGNATURE_INCORRECT = "Signature is incorrect";

  // ------------------------------ NOTIFICATION TYPE -------------------------------------
  public static final String NOTIFICATION_TYPE_NOT_FOUND = "Notification type not found";
  public static final String NOTIFICATION_TYPE_NAME_MANDATORY = "Notification type name is mandatory";
  public static final String NOTIFICATION_TYPE_DESCRIPTION_MANDATORY = "Notification type description is mandatory";
  public static final String NOTIFICATION_TYPE_TITLE_TEMPLATE_MANDATORY = "Notification type title template is mandatory";
  public static final String NOTIFICATION_TYPE_CONTENT_TEMPLATE_MANDATORY = "Notification type content template is mandatory";
  public static final String NOTIFICATION_TYPE_DELETE_SUCCESS = "Notification type deleted";

  // ------------------------------ NOTIFICATION -------------------------------------
  public static final String NOTIFICATION_TITLE_MANDATORY = "Notification title is mandatory";
  public static final String NOTIFICATION_CONTENT_MANDATORY = "Notification content is mandatory";
  public static final String NOTIFICATION_NOT_FOUND = "Notification not found";

  // ------------------------------ SUBSCRIPTION -------------------------------------
  public static final String FOLLOW_SUCCESS = "Follow success";
  public static final String FOLLOW_FAILED = "Follow failed";
  public static final String UN_FOLLOW_SUCCESS = "Unfollow success";
  public static final String UN_FOLLOW_FAILED = "Unfollow failed";

  // ------------------------------ SHIPDATA -------------------------------------
  public static final String SHIP_DATA_ORDER_EXISTED = "Ship order existed";
  public static final int SHIP_DATA_ORDER_EXISTED_CODE = 80001;
  public static final String SHIP_DATA_ORDER_NOT_FOUND = "Ship order not found";
  public static final int SHIP_DATA_ORDER_NOT_FOUND_CODE = 80002;
  public static final String SHIP_DATA_ORDER_CANCEL_NOT_ALLOW = "Ship order can not be canceled";
  public static final int SHIP_DATA_ORDER_CANCEL_NOT_ALLOW_CODE = 80003;
  public static final String SHIP_DATA_STATUS_NOT_FOUND = "Ship status not found";
  public static final int SHIP_DATA_STATUS_NOT_FOUND_CODE = 80004;

  // ------------------------------ JWT -------------------------------------
  public static final String JWT_TOKEN_MISSING = "Jwt token is missing";
  public static final String JWT_CLAIM_ID = "id";
  public static final String JWT_CLAIM_FIRST_NAME = "fname";
  public static final String JWT_CLAIM_LAST_NAME = "lname";
  public static final String JWT_CLAIM_ROLE = "role";

  // ------------------------------ SECURITY -------------------------------------
  public static final String USERNAME_OR_PASSWORD_INCORRECT = "Login failed: username or password is incorrect";
  public static final String USERNAME_OR_PASSWORD_MISSING = "Login failed: username or password is missing";
  public static final String ACCOUNT_INACTIVE = "Login failed: your account has not been activated";
  public static final String ACCOUNT_BANNED = "Login failed: your account has been banned";
  public static final String UNEXPECTED_ERROR = "Something wrong happened please try again later!";

  // ------------------------------ EMAIL -------------------------------------
  public static final String VERIFICATION_EMAIL_SUBJECT = "Verification for reset password";
}
