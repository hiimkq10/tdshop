package com.hcmute.tdshop.dto.shipservices.lalamove;

import com.hcmute.tdshop.dto.shipservices.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stop {
  String stopId;
  Coordinate coordinates;
  String address;
  String name;
  String phone;
}
