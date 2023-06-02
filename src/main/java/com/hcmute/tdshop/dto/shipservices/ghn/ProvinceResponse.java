package com.hcmute.tdshop.dto.shipservices.ghn;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceResponse {
  long code;
  String message;
  List<ProvinceDto> data;
}
