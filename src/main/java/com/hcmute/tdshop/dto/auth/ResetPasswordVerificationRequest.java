package com.hcmute.tdshop.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordVerificationRequest {
  @JsonProperty("Email")
  @Email(message = ApplicationConstants.USER_EMAIL_FORMAT_INVALID)
  @Size(max = 100, message = ApplicationConstants.USER_EMAIL_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.USER_EMAIL_MANDATORY)
  private String email;

  @JsonProperty("Token")
  @NotBlank(message = ApplicationConstants.TOKEN_MANDATORY)
  @Size(min = 6, max = 6, message = ApplicationConstants.TOKEN_INVALID)
  private String token;
}
