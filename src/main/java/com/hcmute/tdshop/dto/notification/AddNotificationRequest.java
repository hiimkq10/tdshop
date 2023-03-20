package com.hcmute.tdshop.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddNotificationRequest {

  @JsonProperty("Content")
  @NotBlank(message = ApplicationConstants.NOTIFICATION_CONTENT_MANDATORY)
  private String content;

  @JsonProperty("SendAll")
  private Boolean sendAll = false;
}
