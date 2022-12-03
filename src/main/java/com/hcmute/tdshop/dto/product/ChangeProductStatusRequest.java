package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class ChangeProductStatusRequest {
  @JsonProperty("ProductId")
  @Positive(message = ApplicationConstants.PRODUCT_ID_INVALID)
  private long productId;

  @JsonProperty("StatusId")
  @Positive(message = ApplicationConstants.PRODUCT_STATUS_INVALID)
  private long statusId;
}
