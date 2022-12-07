package com.hcmute.tdshop.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddEmployeeRequest {
  @JsonProperty("FirstName")
  @Size(max = 50, message = ApplicationConstants.USER_FIRST_NAME_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_FIRST_NAME_MANDATORY)
  private String firstName;

  @JsonProperty("LastName")
  @Size(max = 50, message = ApplicationConstants.USER_LAST_NAME_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_LAST_NAME_MANDATORY)
  private String lastName;

  @JsonProperty("Email")
  @Email(message = ApplicationConstants.USER_EMAIL_FORMAT_INVALID)
  @Size(max = 100, message = ApplicationConstants.USER_EMAIL_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_EMAIL_MANDATORY)
  private String email;

  @JsonProperty("Phone")
  @Size(max = 10, message = ApplicationConstants.USER_PHONE_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_PHONE_MANDATORY)
  private String phone;

  @JsonProperty("Username")
  @Size(max = 50, message = ApplicationConstants.USER_USERNAME_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_USERNAME_MANDATORY)
  private String username;

  @JsonProperty("Password")
  @Size(max = 30, message = ApplicationConstants.USER_PASSWORD_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_PASSWORD_MANDATORY)
  private String password;

  @JsonProperty("Salary")
  @PositiveOrZero(message = ApplicationConstants.USER_SALARY_INVALID)
  private long salary;
}
