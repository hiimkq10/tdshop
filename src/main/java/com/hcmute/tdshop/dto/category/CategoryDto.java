package com.hcmute.tdshop.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryDto {
  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Name")
  private String name;
}
