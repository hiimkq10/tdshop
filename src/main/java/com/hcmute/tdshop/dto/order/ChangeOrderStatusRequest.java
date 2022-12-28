package com.hcmute.tdshop.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangeOrderStatusRequest {
  @JsonProperty("OrderId")
  private long orderId;

  @JsonProperty("StatusId")
  private long statusId;
}
