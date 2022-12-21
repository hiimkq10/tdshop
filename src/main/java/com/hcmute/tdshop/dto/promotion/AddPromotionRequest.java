package com.hcmute.tdshop.dto.promotion;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class AddPromotionRequest {

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("DiscountRate")
  private double discountRate;

  @JsonProperty("StartDate")
  private String startDate;

  @JsonProperty("EndDate")
  private String endDate;

  @JsonProperty("DeletedAt")
  private String deletedAt;

  @JsonProperty("Categories")
  private Set<Long> setOfCategories;
}
