package com.hcmute.tdshop.dto.product;

import lombok.Data;

@Data
public class ProductAttributeDto {
  private long id;
  private String name;
  private String value;
  private int priority;
}
