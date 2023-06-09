package com.hcmute.tdshop.dto.shipservices.ghn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderResponse {
  long code;
  String message;
  GetOrderData data;
}
