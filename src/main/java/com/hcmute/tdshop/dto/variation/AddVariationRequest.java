package com.hcmute.tdshop.dto.variation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddVariationRequest {

  @JsonProperty("Name")
  @NotBlank(message = ApplicationConstants.BRAND_NAME_MANDATORY)
  @Size(max = 100, message = ApplicationConstants.BRAND_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("MasterCategory")
  private long masterCategoryId;

  @JsonProperty("VariationOptions")
  private Set<String> setOfVarirationOptionValues;
}
