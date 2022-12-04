package com.hcmute.tdshop.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class CategoryResponse {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("ChildCategories")
  private List<CategoryDto> listOfCategories;
}
