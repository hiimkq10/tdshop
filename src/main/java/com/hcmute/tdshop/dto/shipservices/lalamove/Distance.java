package com.hcmute.tdshop.dto.shipservices.lalamove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distance {
  Double value;
  String unit;

  public void setValue(String value) {
    this.value = Double.parseDouble(value);
  }
}
