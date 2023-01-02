package com.hcmute.tdshop.dto.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBrandRequest {
  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.BRAND_NAME_SIZE_INVALID)
  private String name;
}
