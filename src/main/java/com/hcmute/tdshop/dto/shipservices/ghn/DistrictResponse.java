package com.hcmute.tdshop.dto.shipservices.ghn;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictResponse {
  long code;
  String message;
  List<DistrictDto> data;
}
