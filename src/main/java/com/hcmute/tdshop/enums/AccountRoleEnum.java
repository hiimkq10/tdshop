package com.hcmute.tdshop.enums;

public enum AccountRoleEnum {
  ROLE_ADMIN(1, "ROLE_ADMIN"),
  ROLE_EMPLOYEE(2,"ROLE_EMPLOYEE"),
  ROLE_USER(3, "ROLE_USER");

  private final long id;
  private final String name;

  AccountRoleEnum(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public static AccountRoleEnum getOrderStatusById(int id) {
    for (AccountRoleEnum o : values()) {
      if (o.id == id) {
        return o;
      }
    }
    return null;
  }
}
