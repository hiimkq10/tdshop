package com.hcmute.tdshop.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemDto {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Price")
  private double price;

  @JsonProperty("Quantity")
  private int quantity;

  @JsonProperty("Product")
  private CartProductDto product;
}
