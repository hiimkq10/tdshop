package com.hcmute.tdshop.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderDetailDto {
  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Price")
  private double price;

  @JsonProperty("DiscountRate")
  private double discountRate;

  @JsonProperty("FinalRate")
  private double finalPrice;

  @JsonProperty("Quantity")
  private int quantity;

  @JsonProperty("ProductId")
  private Long productId;

  @JsonProperty("Sku")
  private String sku;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("ImageUrl")
  private String imageUrl;
}
