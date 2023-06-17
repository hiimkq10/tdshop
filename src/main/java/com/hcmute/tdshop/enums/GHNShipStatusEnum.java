package com.hcmute.tdshop.enums;

public enum GHNShipStatusEnum {
  GHN_NOT_CREATED("not_created", "Không có đơn hàng"),
  GHN_READY_TO_PICK("ready_to_pick", "Đơn hàng vừa được tạo"),
  GHN_PICKING("picking", "Shipper đang đến lấy hàng"),
  GHN_CANCEL("cancel", "Đơn hàng đã hủy"),
  GHN_MONEY_COLLECT_PICKING("money_collect_picking", "Shipper đang làm việc với người bán"),
  GHN_PICKED("picked", "Shipper đã lấy hàng"),
  GHN_STORING("storing", "Đơn hàng đang được lữu trữ trong kho của GHN"),
  GHN_TRANSPORTING("transporting", "Đơn hàng đang được vận chuyển"),
  GHN_SORTING("sorting", "Đang phân loại"),
  GHN_DELIVERING("delivering", "Đang giao"),
  GHN_MONEY_COLLECT_DELIVERING("money_collect_delivering", "Shipper đang làm việc với người mua"),
  GHN_DELIVERED("delivered", "Đã giao"),
  GHN_DELIVERY_FAIL("delivery_fail", "Giao hàng thất bại"),
  GHN_WAITING_TO_RETURN("waiting_to_return", "Chờ giao hàng"),
  GHN_RETURN("return", "Chờ trả hàng lại cho shop"),
  GHN_RETURN_TRANSPORTING("return_transporting", "Đang vận chuyển (trả hàng shop)"),
  GHN_RETURN_SORTING("return_sorting", "Đang phân loại (trả hàng shop)"),
  GHN_RETURNING("returning", "Đang giao (trả hàng shop)"),
  GHN_RETURN_FAIL("return_fail", "Trả hàng shop thất bại"),
  GHN_RETURNED("returned", "Đã trả hàng cho shop"),
  GHN_EXCEPTIOM("exception", "Lỗi"),
  GHN_DAMAGE("damage", "Hàng hóa bị hư hại"),
  GHN_LOST("lost", "Hàng hóa bị mất");

  private String code;
  private String description;

  GHNShipStatusEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return this.code;
  }

  public String getDescription() {
    return this.description;
  }

  public static GHNShipStatusEnum getShipStatusEnumByCode(String code) {
    for (GHNShipStatusEnum s : values()) {
      if (s.getCode().equals(code)) {
        return s;
      }
    }
    return null;
  }
}
