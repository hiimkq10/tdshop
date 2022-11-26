package com.hcmute.tdshop.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Data;

@Data
public class CartDto {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("CartItems")
  private Set<CartItemDto> setOfCartItems;
}
