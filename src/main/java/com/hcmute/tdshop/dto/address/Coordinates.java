package com.hcmute.tdshop.dto.address;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {
  List<Coordinate> coordinates;
}
