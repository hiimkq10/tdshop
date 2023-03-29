package com.hcmute.tdshop.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDto {
  private Long id;
  private String name;

  @JsonIgnore
  private double dValue;
  private String value;
  private long total;

  public RatingDto(long id, String name, double value, long total) {
    this.id = id;
    this.name = name;
    this.dValue = value;
    this.total = total;
    this.value = String.format("%,.2f", value);
  }
}
