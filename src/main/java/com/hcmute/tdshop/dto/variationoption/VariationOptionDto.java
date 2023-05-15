package com.hcmute.tdshop.dto.variationoption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariationOptionDto {
  @JsonProperty("Id")
  private Long id = 0L;

  @JsonProperty("Value")
  @Size(max = 100, message = ApplicationConstants.VARIATION_OPTION_VALUE_SIZE_INVALID)
  private String value;
}
