package com.hcmute.tdshop.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
  private Long id;
  private String name;
  private Long amount;
  @JsonIgnore
  private double dTotal;
  private String total;

  public ProductDto(Long id, String name, Long amount, double total) {
    this.id = id;
    this.name = name;
    this.amount = amount;
    this.dTotal = total;
    this.total = String.format("%,.2f", total);
  }
}
