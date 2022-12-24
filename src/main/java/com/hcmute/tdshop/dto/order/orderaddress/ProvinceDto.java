package com.hcmute.tdshop.dto.order.orderaddress;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProvinceDto {
  @JsonProperty("Id")
  private long id;

  @JsonProperty("Name")
  private String name;
}
