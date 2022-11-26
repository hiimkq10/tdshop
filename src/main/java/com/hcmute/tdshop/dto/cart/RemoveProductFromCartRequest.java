package com.hcmute.tdshop.dto.cart;

import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class RemoveProductFromCartRequest {
  @Positive
  private long userId;
  @Positive
  private long productId;
}
