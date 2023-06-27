package com.hcmute.tdshop.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawPolygon {
  String place_id;
  @JsonProperty("display_name")
  String displayName;
  String category;
  String type;
  RawGeoJson geojson;
}
