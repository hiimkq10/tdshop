package com.hcmute.tdshop.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequest {
  @JsonProperty("Name")
  @Size(max = 50, message = ApplicationConstants.BRAND_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("ParentCategory")
  @PositiveOrZero(message = ApplicationConstants.PARENT_CATEGORY_ID_INVALID)
  private long parentCategoryId;
}
