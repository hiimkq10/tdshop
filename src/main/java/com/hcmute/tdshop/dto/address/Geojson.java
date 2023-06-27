package com.hcmute.tdshop.dto.address;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Geojson {
  String type;
  List<List<List<Double>>> coordinates;

  public static Geojson toGeoJson(RawGeoJson rawGeoJson) {
    if (rawGeoJson == null) {
      return null;
    }
    try {
      List<List<List<Double>>> list = rawGeoJson.coordinates.stream().map(item -> (List<List<Double>>) item).collect(Collectors.toList());
      return new Geojson(rawGeoJson.getType(), list);
    }
    catch (Exception e) {
      return new Geojson(rawGeoJson.getType(), null);
    }
  }
}
