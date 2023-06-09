package com.hcmute.tdshop.dto.shipservices.lalamove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipent {
  String stopId;
  private String name;
  private String phone;
}
