package com.hcmute.tdshop.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressToLatAndLngDto {
  Long wardsId;
  String addressDetail;
}
