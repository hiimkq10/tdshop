package com.hcmute.tdshop.dto.order.orderaddress;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DistrictDto {
  @JsonProperty("Id")
  private long id;

  @JsonProperty("Name")
  private String name;
}
