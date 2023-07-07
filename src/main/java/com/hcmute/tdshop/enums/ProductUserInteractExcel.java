package com.hcmute.tdshop.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUserInteractExcel {
  private Long orderId;
  private Long userId;
  private Long productId;
  private String categoryIds;
  private Integer bought;
  private Integer rating;
}
