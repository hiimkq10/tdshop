package com.hcmute.tdshop.dto.shipservices.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTimeData {
  @JsonProperty("leadtime")
  long leadtime;

  @JsonProperty("order_date")
  long orderDate;
}
