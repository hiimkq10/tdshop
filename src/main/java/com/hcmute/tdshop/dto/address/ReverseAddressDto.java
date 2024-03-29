package com.hcmute.tdshop.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReverseAddressDto {
  @JsonProperty("display_name")
  String displayName;
//  AddressDto address;
  Map<String, String> address;
}
