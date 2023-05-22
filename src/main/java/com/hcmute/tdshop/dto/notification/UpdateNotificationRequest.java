package com.hcmute.tdshop.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNotificationRequest {
  @JsonProperty("Title")
  private String title;

  @JsonProperty("Content")
  private String content;

  @JsonProperty("SendAll")
  private Boolean sendAll;

  @JsonProperty("Url")
  private String url;
}
