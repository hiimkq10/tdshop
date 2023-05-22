package com.hcmute.tdshop.dto.notificationtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNotificationTypeDto {
  @JsonProperty("Name")
  @NotBlank(message = ApplicationConstants.NOTIFICATION_TYPE_NAME_MANDATORY)
  private String name;

  @JsonProperty("Description")
  private String description;
  @NotBlank(message = ApplicationConstants.NOTIFICATION_TYPE_TITLE_TEMPLATE_MANDATORY)

  @JsonProperty("TitleTemplate")
  private String titleTemplate;
  @NotBlank(message = ApplicationConstants.NOTIFICATION_TYPE_CONTENT_TEMPLATE_MANDATORY)

  @JsonProperty("ContentTemplate")
  private String contentTemplate;
}
