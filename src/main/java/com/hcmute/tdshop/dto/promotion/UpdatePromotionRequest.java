package com.hcmute.tdshop.dto.promotion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.annotations.ValueOfLocalDateTime;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Set;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class UpdatePromotionRequest {

  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.PROMOTION_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("Description")
  @Size(max = 250, message = ApplicationConstants.PROMOTION_DESCRIPTION_SIZE_INVALID)
  private String description;

  @JsonProperty("DiscountRate")
  @PositiveOrZero
  private double discountRate;

  @JsonProperty("StartDate")
  @ValueOfLocalDateTime
  private String startDate;

  @JsonProperty("EndDate")
  @ValueOfLocalDateTime
  private String endDate;

  @JsonProperty("Categories")
  private Set<Long> setOfCategories;
}
