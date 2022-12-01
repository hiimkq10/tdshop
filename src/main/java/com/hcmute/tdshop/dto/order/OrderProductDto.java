package com.hcmute.tdshop.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderProductDto {
  @JsonProperty("ProductId")
  @Positive(message = ApplicationConstants.PRODUCT_ID_INVALID)
  private long productId;

  @JsonProperty("Quantity")
  @Positive(message = ApplicationConstants.PRODUCT_QUANTITY_MUST_BIGGER_THAN_0)
  private int quantity;
}
