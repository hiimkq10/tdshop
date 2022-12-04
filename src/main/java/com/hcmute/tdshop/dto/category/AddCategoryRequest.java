package com.hcmute.tdshop.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCategoryRequest {

  @JsonProperty("Name")
  @NotBlank(message = ApplicationConstants.CATEGORY_NAME_MANDATORY)
  @Size(max = 50, message = ApplicationConstants.CATEGORY_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("ParentCategory")
  private long parentCategoryId;

  @JsonProperty("MasterCategory")
  private long masterCategoryId;
}
