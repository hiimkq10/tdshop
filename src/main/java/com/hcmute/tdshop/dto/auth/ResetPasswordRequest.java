package com.hcmute.tdshop.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
  @JsonProperty("Email")
  @Email(message = ApplicationConstants.USER_EMAIL_FORMAT_INVALID)
  @Size(max = 100, message = ApplicationConstants.USER_EMAIL_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_EMAIL_MANDATORY)
  private String email;

  @JsonProperty("NewPassword")
  @NotBlank(message = ApplicationConstants.NEW_PASSWORD_MISSING)
  @Size(max = 30, message = ApplicationConstants.NEW_PASSWORD_MAX_SIZE_INVALID)
  private String newPassword;

  @JsonProperty("ConfirmPassword")
  @NotBlank(message = ApplicationConstants.CONFIRM_PASSWORD_MISSING)
  @Size(max = 30, message = ApplicationConstants.CONFIRM_PASSWORD_MAX_SIZE_INVALID)
  private String confirmPassword;
}
