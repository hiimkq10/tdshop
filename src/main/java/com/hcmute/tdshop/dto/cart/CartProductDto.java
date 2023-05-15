package com.hcmute.tdshop.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartProductDto {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Sku")
  private String sku;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("ImageUrl")
  private String imageUrl;

  @JsonProperty("Price")
  private String price;

  @JsonProperty("Discount")
  private CartProductPromotionDto productPromotion;

  @JsonProperty("Total")
  private int total;
}
