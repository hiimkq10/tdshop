package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDataResponse {
  @JsonProperty("district_encode")
  private String districtEncode;

  @JsonProperty("expected_delivery_time")
  private String expectedDeliveryTime;

  @JsonProperty("fee")
  private FeeDto fee;

  @JsonProperty("order_code")
  private String orderCode;

  @JsonProperty("sort_code")
  private String sortCode;

  @JsonProperty("total_fee")
  private String totalFee;

  @JsonProperty("trans_type")
  private String transType;

  @JsonProperty("ward_encode")
  private String wardEncode;
}
