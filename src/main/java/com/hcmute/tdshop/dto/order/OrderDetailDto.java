package com.hcmute.tdshop.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderDetailDto {
  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Price")
  private String price;

  @JsonProperty("DiscountRate")
  private double discountRate;

  @JsonProperty("FinalPrice")
  private String finalPrice;

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
