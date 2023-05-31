package com.hcmute.tdshop.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
  String suburb;
  String city;
  String state;
  String postcode;
  String country;
  String country_code;
}
