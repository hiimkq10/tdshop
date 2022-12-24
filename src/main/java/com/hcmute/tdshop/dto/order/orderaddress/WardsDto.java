package com.hcmute.tdshop.dto.order.orderaddress;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WardsDto {
  @JsonProperty("Id")
  private long id;

  @JsonProperty("Name")
  private String name;
}
