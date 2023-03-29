package com.hcmute.tdshop.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueStatisticDto {
  private String date;
  @JsonIgnore
  private double dRevenue;
  private String revenue;

  private String percent;

  public RevenueStatisticDto(String date, double revenue) {
    this.date = date;
    this.dRevenue = revenue;
    this.revenue = BigDecimal.valueOf(revenue).toPlainString();

  }
}
