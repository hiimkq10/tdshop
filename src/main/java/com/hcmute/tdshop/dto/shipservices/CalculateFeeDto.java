package com.hcmute.tdshop.dto.shipservices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateFeeDto {
  @JsonProperty("Products")
  @NotNull(message = ApplicationConstants.ORDER_PRODUCTS_EMPTY)
  @Size(min = 1, message = ApplicationConstants.ORDER_PRODUCTS_EMPTY)
  private Set<OrderProductDto> setOfProducts;

  @JsonProperty("Ship")
  @Positive(message = ApplicationConstants.SHIP_ID_INVALID)
  private long shipId;

  @JsonProperty("Address")
  @Positive(message = ApplicationConstants.ADDRESS_ID_INVALID)
  private long addressId;
}
