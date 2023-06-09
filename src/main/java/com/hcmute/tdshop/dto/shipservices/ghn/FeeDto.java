package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeDto {
  @JsonProperty("coupon")
  private double coupon;

  @JsonProperty("insurance")
  private double insurance;

  @JsonProperty("main_service")
  private double mainService;

  @JsonProperty("r2s")
  private double r2s;

  @JsonProperty("return")
  private double totalReturn;

  @JsonProperty("station_do")
  private double stationDo;

  @JsonProperty("station_pu")
  private double stationPu;

  @JsonProperty("cod_failed_fee")
  private double codFailedFee;
}
