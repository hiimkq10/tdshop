package com.hcmute.tdshop.dto.shipservices.lalamove;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
  String orderId;
  String quotationId;
  PriceBreakdown priceBreakdown;
  String driverId;
  String shareLink;
  String status;
  Distance distance;
  List<Stop> stops;
}
