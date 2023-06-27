package com.hcmute.tdshop.enums;

public enum AdministrativeTypeEnum {
  TINH(1, "TINH", "Tỉnh", 1),
  THANH_PHO(2, "THANH_PHO", "Thành phố", 2),
  QUAN(3, "QUAN", "Quận", 3),
  HUYEN(4, "HUYEN", "Huyện", 4),
  THI_XA(5, "THI_XA", "Thị xã", 5),
  XA(6, "XA", "Xã", 6),
  PHUONG(7, "PHUONG", "Phường", 7),
  THI_TRAN(8, "THI_TRAN", "Thị trấn", 8);

  private final long id;
  private final String code;
  private final String name;
  private final int priority;

  AdministrativeTypeEnum(long id, String code, String name, int priority) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.priority = priority;
  }

  public long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public int getPriority() {
    return priority;
  }

  public static AdministrativeTypeEnum getAdministrativeTypeEnumByName(String name) {
    for (AdministrativeTypeEnum a : values()) {
      if (name.trim().toLowerCase().startsWith(a.getName().trim().toLowerCase())) {
        return a;
      }
    }
    return null;
  }
}
