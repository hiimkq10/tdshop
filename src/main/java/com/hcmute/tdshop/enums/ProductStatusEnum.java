package com.hcmute.tdshop.enums;

public enum ProductStatusEnum {
  HIDE(1, "Đã ẩn"),
  ONSALE(2, "Đang bán"),
  OUT_OF_ORDER(3, "Ngừng bán");

  private final long id;
  private final String name;

  ProductStatusEnum(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public static ProductStatusEnum getProductStatusById(int id) {
    for (ProductStatusEnum p : values()) {
      if (p.id == id) {
        return p;
      }
    }
    return null;
  }
}
