package com.hcmute.tdshop.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.dto.order.OrderProductDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckCartProductValidRequest {
  @JsonProperty("Products")
  List<OrderProductDto> products;
}
