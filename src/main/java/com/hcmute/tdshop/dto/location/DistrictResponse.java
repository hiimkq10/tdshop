package com.hcmute.tdshop.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictResponse {

  private Long id;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("ShortName")
  private String shortName;

  @JsonProperty("Type")
  private String type;

  @JsonProperty("TypePriority")
  private int typePriority;

  private ProvinceResponse province;
}
