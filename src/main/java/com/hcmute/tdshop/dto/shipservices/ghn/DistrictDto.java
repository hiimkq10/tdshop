package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictDto {
  @JsonProperty("DistrictID")
  long DistrictID;

  @JsonProperty("DistrictName")
  String DistrictName;

  @JsonProperty("Code")
  String Code;

  @JsonProperty("Type")
  long Type;

  @JsonProperty("SupportType")
  long SupportType;

  @JsonProperty("NameExtension")
  List<String> NameExtension;
}
