package com.hcmute.tdshop.dto.shipservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSize {
  private double length;
  private double width;
  private double height;
  private double weight;
}
