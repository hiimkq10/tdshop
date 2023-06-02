package com.hcmute.tdshop.dto.shipservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductParameters {
  private String name;
  private int quantity;
  private double length = 0.0;
  private double width = 0.0;
  private double height = 0.0;
  private double weight = 0.0;
}
