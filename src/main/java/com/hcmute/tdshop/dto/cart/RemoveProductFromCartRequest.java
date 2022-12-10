package com.hcmute.tdshop.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class RemoveProductFromCartRequest {
  @JsonProperty("ProductId")
  @Positive(message = ApplicationConstants.PRODUCT_ID_INVALID)
  private long productId;
}
