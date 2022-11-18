package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class SimpleProductDto {
  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Sku")
  private String sku;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Price")
  private double price;
}
