package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderData {
  @JsonProperty("shop_id")
  long shopId;

  @JsonProperty("client_id")
  long clientId;

  @JsonProperty("return_name")
  String returnName;

  @JsonProperty("return_phone")
  String returnPhone;

  @JsonProperty("return_address")
  String returnAddress;

  @JsonProperty("return_ward_code")
  String returnWardCode;

  @JsonProperty("return_district_id")
  long returnDistrictId;

  @JsonProperty("from_name")
  String fromName;

  @JsonProperty("from_phone")
  String fromPhone;

  @JsonProperty("from_address")
  String fromAddress;

  @JsonProperty("from_ward_code")
  String fromWardCode;

  @JsonProperty("from_district_id")
  long fromDistrictId;

  @JsonProperty("deliver_station_id")
  long deliverStationId;

  @JsonProperty("to_name")
  String toName;

  @JsonProperty("to_phone")
  String toPhone;

  @JsonProperty("to_address")
  String toAddress;

  @JsonProperty("to_ward_code")
  String toWardCode;

  @JsonProperty("to_district_id")
  long toDistrictId;

  @JsonProperty("weight")
  long weight;

  @JsonProperty("length")
  long length;

  @JsonProperty("width")
  long width;

  @JsonProperty("height")
  long height;

  @JsonProperty("converted_weight")
  long convertedWeight;

  @JsonProperty("service_type_id")
  long serviceTypeId;

  @JsonProperty("service_id")
  long serviceId;

  @JsonProperty("payment_type_id")
  long paymentTypeId;

  @JsonProperty("custom_service_fee")
  long customServiceFee;

  @JsonProperty("cod_amount")
  long codAmount;

  @JsonProperty("cod_collect_date")
  long codCollectDate;

  @JsonProperty("cod_transfer_date")
  long codTransferDate;

  @JsonProperty("required_note")
  long requiredNote;

  @JsonProperty("content")
  long content;

  @JsonProperty("order_code")
  long orderCode;

  @JsonProperty("tag")
  List<String> tags;

  @JsonProperty("log")
  List<Log> logs;
}
