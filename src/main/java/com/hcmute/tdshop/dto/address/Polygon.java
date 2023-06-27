package com.hcmute.tdshop.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Polygon {
  String place_id;
  @JsonProperty("display_name")
  String displayName;
  String category;
  String type;
  Geojson geojson;

  public static Polygon toPolygon(RawPolygon rawPolygon) {
    if (rawPolygon == null) {
      return null;
    }
    return new Polygon(rawPolygon.getPlace_id(), rawPolygon.getDisplayName(), rawPolygon.getCategory(), rawPolygon.getType(), Geojson.toGeoJson(rawPolygon.getGeojson()));
  }
}
