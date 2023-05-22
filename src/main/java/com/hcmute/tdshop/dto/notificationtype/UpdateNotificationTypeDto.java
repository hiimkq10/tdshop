package com.hcmute.tdshop.dto.notificationtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNotificationTypeDto {
  @JsonProperty("Name")
  private String name;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("TitleTemplate")
  private String titleTemplate;

  @JsonProperty("ContentTemplate")
  private String contentTemplate;
}
