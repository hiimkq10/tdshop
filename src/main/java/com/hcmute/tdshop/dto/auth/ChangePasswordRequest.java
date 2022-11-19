package com.hcmute.tdshop.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
  @JsonProperty("CurrentPassword")
  @NotBlank(message = ApplicationConstants.CURRENT_PASSWORD_MISSING)
  @Size(max = 30, message = ApplicationConstants.CURRENT_PASSWORD_MAX_SIZE_INVALID)
  private String currentPassword;

  @JsonProperty("NewPassword")
  @NotBlank(message = ApplicationConstants.NEW_PASSWORD_MISSING)
  @Size(max = 30, message = ApplicationConstants.NEW_PASSWORD_MAX_SIZE_INVALID)
  private String newPassword;

  @JsonProperty("ConfirmPassword")
  @NotBlank(message = ApplicationConstants.CONFIRM_PASSWORD_MISSING)
  @Size(max = 30, message = ApplicationConstants.CONFIRM_PASSWORD_MAX_SIZE_INVALID)
  private String confirmPassword;
}
