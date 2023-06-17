package com.hcmute.tdshop.dto.shipservices.ghn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTimeResponse {
  long code;
  String message;
  DeliveryTimeData data;
}
