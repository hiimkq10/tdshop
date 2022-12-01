package com.hcmute.tdshop.enums;

public enum PaymentMethodEnum {
  COD(1, "Thanh toán tiền mặt khi nhận hàng"),
  MOMO(2, "Thanh toán bằng ví MoMo"),
  VNPAY(3, "Thanh toán bằng VNPAY");

  private final long id;
  private final String name;

  PaymentMethodEnum(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public static PaymentMethodEnum getPaymentMethodById(int id) {
    for (PaymentMethodEnum p : values()) {
      if (p.id == id) {
        return p;
      }
    }
    return null;
  }
}
