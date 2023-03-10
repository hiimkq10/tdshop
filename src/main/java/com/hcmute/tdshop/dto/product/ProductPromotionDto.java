package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductPromotionDto {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("DiscountRate")
  private double discountRate;
}
