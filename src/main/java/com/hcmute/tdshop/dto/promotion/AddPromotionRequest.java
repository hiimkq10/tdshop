package com.hcmute.tdshop.dto.promotion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.annotations.ValueOfLocalDateTime;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddPromotionRequest {

  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.PROMOTION_NAME_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.PROMOTION_NAME_MANDATORY)
  private String name;

  @JsonProperty("Description")
  @Size(max = 250, message = ApplicationConstants.PROMOTION_DESCRIPTION_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.PROMOTION_DESCRIPTION_MANDATORY)
  private String description;

  @JsonProperty("DiscountRate")
  @Positive(message = ApplicationConstants.PROMOTION_DISCOUNT_RATE_SMALLER_THAN_0)
  private double discountRate;

  @JsonProperty("StartDate")
  @ValueOfLocalDateTime()
  private String startDate;

  @JsonProperty("EndDate")
  @ValueOfLocalDateTime()
  private String endDate;

  @JsonProperty("Categories")
  private Set<Long> setOfCategories;
}
