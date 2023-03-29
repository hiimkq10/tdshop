package com.hcmute.tdshop.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDto {
  private long id;
  private String name;
  private long amount;
  private String percent;

  public OrderDto(long id, String name, long amount) {
    this.id = id;
    this.name = name;
    this.amount = amount;
  }
}
