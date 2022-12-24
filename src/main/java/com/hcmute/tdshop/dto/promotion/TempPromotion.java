package com.hcmute.tdshop.dto.promotion;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempPromotion {

  private double maxDiscountRate;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private List<Double> listOfDiscountRates;
}
