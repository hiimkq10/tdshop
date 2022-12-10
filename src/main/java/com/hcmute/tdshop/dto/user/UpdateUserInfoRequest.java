package com.hcmute.tdshop.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.annotations.ValueOfLocalDate;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserInfoRequest {

  @JsonProperty("FirstName")
  @Size(max = 50, message = ApplicationConstants.USER_FIRST_NAME_SIZE_INVALID)
  private String firstName;

  @JsonProperty("LastName")
  @Size(max = 50, message = ApplicationConstants.USER_LAST_NAME_SIZE_INVALID)
  private String lastName;

  @JsonProperty("Email")
  @Size(max = 100, message = ApplicationConstants.USER_EMAIL_SIZE_INVALID)
  @Email(message = ApplicationConstants.USER_EMAIL_FORMAT_INVALID)
  private String email;

  @JsonProperty("Phone")
  @Size(max = 10, message = ApplicationConstants.USER_PHONE_SIZE_INVALID)
  private String phone;

  @JsonProperty("Birthdate")
  @ValueOfLocalDate()
  private String birthdate;

  @JsonProperty("Gender")
  private Boolean gender;
}
