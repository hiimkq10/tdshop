package com.hcmute.tdshop.enums;

public enum LalamoveWeightEnum {
  LESS_THAN_10_KG("LESS_THAN_10_KG", "LESS_THAN_10_KG"),
  BETWEEN_10_KG_AND_30_KG("10_KG_TO_30_KG","10_KG_TO_30_KG"),
  BETWEEN_30_KG_AND_50_KG("30_KG_TO_50_KG", "30_KG_TO_50_KG"),
  MORE_THAN_50_KG("MORE_THAN_50_KG", "MORE_THAN_50_KG");

  private final String code;
  private final String description;

  LalamoveWeightEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return this.code;
  }

  public String getName() {
    return this.description;
  }
}
