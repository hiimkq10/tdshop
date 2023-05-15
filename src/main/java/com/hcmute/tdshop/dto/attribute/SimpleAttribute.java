package com.hcmute.tdshop.dto.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAttribute {
  @JsonProperty("Id")
  private Long id = 0L;

  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.ATTRIBUTE_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("Priority")
  private int priority;
}
