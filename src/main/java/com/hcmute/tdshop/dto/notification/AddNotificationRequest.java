package com.hcmute.tdshop.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddNotificationRequest {

  @JsonProperty("Title")
  @NotBlank(message = ApplicationConstants.NOTIFICATION_TITLE_MANDATORY)
  private String title;

  @JsonProperty("Content")
  @NotBlank(message = ApplicationConstants.NOTIFICATION_CONTENT_MANDATORY)
  private String content;

  @JsonProperty("SendAll")
  private Boolean sendAll = false;

  @JsonProperty("Url")
  private String url;

  @JsonProperty("TypeId")
  private String typeId;

}
