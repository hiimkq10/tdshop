package com.hcmute.tdshop.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.dto.order.orderaddress.AddressDto;
import com.hcmute.tdshop.entity.OrderStatus;
import com.hcmute.tdshop.entity.PaymentMethod;
import com.hcmute.tdshop.entity.Ship;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithShipDataResponse {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("OrderedAt")
  private LocalDateTime orderedAt;

  @JsonProperty("PaymentMethod")
  private PaymentMethod paymentMethod;

  @JsonProperty("Ship")
  private Ship ship;

  @JsonProperty("OrderStatus")
  private OrderStatus orderStatus;

  @JsonProperty("Address")
  private AddressDto address;

  @JsonProperty("OrderDetails")
  private Set<OrderDetailDto> setOfOrderDetailDtos;

  @JsonProperty("ShipStatusCode")
  private String shipStatusCode;

  @JsonProperty("ShipStatusDescription")
  private String ShipStatusDescription;
}
