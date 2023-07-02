package com.hcmute.tdshop.model;

import com.hcmute.tdshop.utils.annotations.ExcelColumnIndex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductExcel {
  @ExcelColumnIndex(index = 0)
  private String sku;

  @ExcelColumnIndex(index = 1)
  private String name;

  @ExcelColumnIndex(index = 2)
  private String imageUrl;

  @ExcelColumnIndex(index = 3)
  private double price;

  @ExcelColumnIndex(index = 4)
  private String description;

  @ExcelColumnIndex(index = 5)
  private String shortDescription;

  @ExcelColumnIndex(index = 6)
  private int total;

  @ExcelColumnIndex(index = 7)
  private Double length;

  @ExcelColumnIndex(index = 8)
  private Double width;

  @ExcelColumnIndex(index = 9)
  private Double height;

  @ExcelColumnIndex(index = 10)
  private Double weight;

  @ExcelColumnIndex(index = 11)
  private String categories;

  @ExcelColumnIndex(index = 12)
  private Long brandId;

  @ExcelColumnIndex(index = 13)
  private String imageUrls;

  @ExcelColumnIndex(index = 14)
  private String variations;
}
