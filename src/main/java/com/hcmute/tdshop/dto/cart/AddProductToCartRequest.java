package com.hcmute.tdshop.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddProductToCartRequest {
  @JsonProperty("UserId")
  @Positive
  private long userId;

  @JsonProperty("ProductId")
  @Positive
  private long productId;
}
