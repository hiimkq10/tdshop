package com.hcmute.tdshop.dto.mastercategory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddMasterCategoryRequest {
  @JsonProperty("Name")
  @NotBlank(message = ApplicationConstants.MASTER_CATEGORY_NAME_MANDATORY)
  @Size(max = 50, message = ApplicationConstants.MASTER_CATEGORY_NAME_SIZE_INVALID)
  private String name;
}
