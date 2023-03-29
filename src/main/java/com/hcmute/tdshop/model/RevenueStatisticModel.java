package com.hcmute.tdshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueStatisticModel {
  private String date;
  private double price;
  private double quantity;
}
