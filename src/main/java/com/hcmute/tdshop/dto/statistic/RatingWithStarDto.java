package com.hcmute.tdshop.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingWithStarDto {
  private Long id;
  private String name;

  @JsonIgnore
  private double dValue;
  private String value;
  private long total;
  private long star1;
  private long star2;
  private long star3;
  private long star4;
  private long star5;

  public RatingWithStarDto(long id, String name, double value, long total, long star1, long star2, long star3, long star4, long star5) {
    this.id = id;
    this.name = name;
    this.dValue = value;
    this.total = total;
    this.value = String.format("%,.2f", value);
    this.star1 = star1;
    this.star2 = star2;
    this.star3 = star3;
    this.star4 = star4;
    this.star5 = star5;
  }
}
