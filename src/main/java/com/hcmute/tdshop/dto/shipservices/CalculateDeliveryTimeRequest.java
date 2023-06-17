package com.hcmute.tdshop.dto.shipservices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateDeliveryTimeRequest {

  @JsonProperty("Address")
  @Positive(message = ApplicationConstants.ADDRESS_ID_INVALID)
  private long addressId;
}
