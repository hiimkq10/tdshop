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
  private long length = 0;
  private long width = 0;
  private long height = 0;
  private long weight = 0;
}
