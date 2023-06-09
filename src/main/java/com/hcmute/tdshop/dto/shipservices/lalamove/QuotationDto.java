package com.hcmute.tdshop.dto.shipservices.lalamove;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationDto {
  String quotationId;
  LocalDateTime scheduleAt;
  LocalDateTime expiresAt;
  String serviceType;
  List<String> specialRequests;
  String language;
  List<Stop> stops;
  Boolean isRouteOptimized;
  PriceBreakdown priceBreakdown;
  Item item;
  Distance distance;
}
