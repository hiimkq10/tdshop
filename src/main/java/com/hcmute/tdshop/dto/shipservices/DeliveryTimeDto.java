package com.hcmute.tdshop.dto.shipservices;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTimeDto {
  LocalDateTime deliveryTime;
}
