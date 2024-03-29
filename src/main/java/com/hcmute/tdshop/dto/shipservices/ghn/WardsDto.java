package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WardsDto {
  @JsonProperty("WardCode")
  long WardCode;

  @JsonProperty("WardName")
  String WardName;

  @JsonProperty("NameExtension")
  List<String> NameExtension;
}
