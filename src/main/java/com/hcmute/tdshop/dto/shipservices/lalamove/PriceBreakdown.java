package com.hcmute.tdshop.dto.shipservices.lalamove;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceBreakdown {
  Double base;
  Double specialRequests;
  Double vat;
  Double totalBeforeOptimization;
  Double totalExcludePriorityFee;
  Double total;
  String currency;
  Double extraMileage;
  Double surcharge;
  Double priorityFee;

  public void setBase(String base) {
    this.base = Double.parseDouble(base);
  }

  public void setSpecialRequests(String specialRequests) {
    this.specialRequests = Double.parseDouble(specialRequests);
  }

  public void setVat(String vat) {
    this.vat = Double.parseDouble(vat);
  }

  public void setTotalBeforeOptimization(String totalBeforeOptimization) {
    this.totalBeforeOptimization = Double.parseDouble(totalBeforeOptimization);
  }

  public void setTotalExcludePriorityFee(String totalExcludePriorityFee) {
    this.totalExcludePriorityFee = Double.parseDouble(totalExcludePriorityFee);
  }

  public void setTotal(String total) {
    this.total = Double.parseDouble(total);
  }

  public void setExtraMileage(String extraMileage) {
    this.extraMileage = Double.parseDouble(extraMileage);
  }

  public void setSurcharge(String surcharge) {
    this.surcharge = Double.parseDouble(surcharge);
  }

  public void setPriorityFee(String priorityFee) {
    this.priorityFee = Double.parseDouble(priorityFee);
  }
}
