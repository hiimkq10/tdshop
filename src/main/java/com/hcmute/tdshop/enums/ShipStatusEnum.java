package com.hcmute.tdshop.enums;

public enum ShipStatusEnum {
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
  GHN_LOST("lost", "Hàng hóa bị mất"),
  LALAMOVE_ASSIGNING_DRIVER("ASSIGNING_DRIVER", "Đang tìm shipper"),
  LALAMOVE_ON_GOING("ON_GOING", "Shipper đã nhận đơn"),
  LALAMOVE_PICKED_UP("PICKED_UP", "Shipper đã lấy hàng"),
  LALAMOVE_COMPLETED("COMPLETED", "Đã giao"),
  LALAMOVE_CANCELED("CANCELED", "Đã hủy"),
  LALAMOVE_REJECTED("REJECTED", "Shipper từ chối đơn hàng"),
  LALAMOVE_EXPIRED("EXPIRED", "Đơn hàng quá hạn");

  private String code;
  private String description;

  ShipStatusEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return this.code;
  }

  public String getDescription() {
    return this.description;
  }
}
