package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceDto {
  @JsonProperty("ProvinceID")
  long ProvinceID;

  @JsonProperty("ProvinceName")
  String ProvinceName;

  @JsonProperty("Code")
  String Code;

  @JsonProperty("NameExtension")
  List<String> NameExtension;
}
