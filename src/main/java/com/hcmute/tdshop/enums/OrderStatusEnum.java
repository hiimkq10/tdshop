package com.hcmute.tdshop.enums;

public enum OrderStatusEnum {
  AWAITINGPAYMENT(1, "Chờ thanh toán"),
  PROCCESSING(2,"Đang xử lý"),
  DELIVERING(3, "Đang vận chuyển"),
  DELIVERED(4, "Đã giao"),
  CANCELED(5, "Đã hủy");

  private final long id;
  private final String name;

  OrderStatusEnum(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public static OrderStatusEnum getOrderStatusById(int id) {
    for (OrderStatusEnum o : values()) {
      if (o.id == id) {
        return o;
      }
    }
    return null;
  }
}
