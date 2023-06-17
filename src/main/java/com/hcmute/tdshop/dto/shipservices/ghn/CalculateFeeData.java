package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateFeeData {
  @JsonProperty("total")
  double total;

  @JsonProperty("service_fee")
  double serviceFee;

  @JsonProperty("insurance_fee")
  double insuranceFee;

  @JsonProperty("pick_station_fee")
  double pickStationFee;

  @JsonProperty("coupon_value")
  double couponValue;

  @JsonProperty("r2s_fee")
  double r2sFee;

  @JsonProperty("document_return")
  double documentReturn;

  @JsonProperty("double_check")
  double doubleCheck;

  @JsonProperty("cod_fee")
  double codFee;

  @JsonProperty("pick_remote_areas_fee")
  double pickRemoteAreasFee;

  @JsonProperty("deliver_remote_areas_fee")
  double deliverRemoteAreasFee;

  @JsonProperty("cod_failed_fee")
  double codFailedFee;
}
