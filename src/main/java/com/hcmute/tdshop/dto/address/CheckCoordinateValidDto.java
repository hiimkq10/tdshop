package com.hcmute.tdshop.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckCoordinateValidDto {
  String lat;
  String lng;
  Long wardsId;
}
