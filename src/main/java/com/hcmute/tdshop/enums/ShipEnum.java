package com.hcmute.tdshop.enums;

public enum ShipEnum {
  FASTDELIVERY(1, "Giao hàng nhanh"),
  NORMALDELIVERY(2, "Giao hàng tiết kiệm"),
  LALAMOVE(3, "Lalamove");
  private final long id;
  private final String name;

  ShipEnum(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public static ShipEnum getShipById(int id) {
    for (ShipEnum s : values()) {
      if (s.id == id) {
        return s;
      }
    }
    return null;
  }
}
