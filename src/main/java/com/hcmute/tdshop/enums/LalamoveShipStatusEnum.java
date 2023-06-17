package com.hcmute.tdshop.enums;

public enum LalamoveShipStatusEnum {
  LALAMOVE_NOT_CREATED("NOT_CREATED", "Không có đơn hàng"),
  LALAMOVE_ASSIGNING_DRIVER("ASSIGNING_DRIVER", "Đang tìm shipper"),
  LALAMOVE_ON_GOING("ON_GOING", "Shipper đã nhận đơn"),
  LALAMOVE_PICKED_UP("PICKED_UP", "Shipper đã lấy hàng"),
  LALAMOVE_COMPLETED("COMPLETED", "Đã giao"),
  LALAMOVE_CANCELED("CANCELED", "Đã hủy"),
  LALAMOVE_REJECTED("REJECTED", "Shipper từ chối đơn hàng"),
  LALAMOVE_EXPIRED("EXPIRED", "Đơn hàng quá hạn");

  private String code;
  private String description;

  LalamoveShipStatusEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return this.code;
  }

  public String getDescription() {
    return this.description;
  }

  public static LalamoveShipStatusEnum getShipStatusEnumByCode(String code) {
    for (LalamoveShipStatusEnum l : values()) {
      if (l.getCode().equals(code)) {
        return l;
      }
    }
    return null;
  }
}
