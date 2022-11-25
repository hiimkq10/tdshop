package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.entity.Image;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SimpleProductDto {
  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Sku")
  private String sku;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Price")
  private double price;

  @JsonProperty("ImageUrl")
  private String imageUrl;

  @JsonProperty("Discount")
  private ProductPromotionDto productPromotion;
}
