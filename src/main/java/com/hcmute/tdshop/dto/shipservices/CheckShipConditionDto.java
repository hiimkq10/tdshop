package com.hcmute.tdshop.dto.shipservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckShipConditionDto {
  boolean result;
  String message;
  int messageCode;
}
