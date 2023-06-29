package com.hcmute.tdshop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeDto {
  private long id;
  private String name;
  private String value;
  private int priority;
  private long attributeId;
}
