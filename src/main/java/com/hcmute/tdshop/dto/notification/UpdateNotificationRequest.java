package com.hcmute.tdshop.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNotificationRequest {

  @JsonProperty("Content")
  @NotBlank(message = ApplicationConstants.MASTER_CATEGORY_NAME_MANDATORY)
  private String content;

  @JsonProperty("SendAll")
  private Boolean sendAll;
}
