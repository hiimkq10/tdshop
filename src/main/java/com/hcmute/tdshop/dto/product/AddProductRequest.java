package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class AddProductRequest {
  @JsonProperty("Name")
  private String name;

  @JsonProperty("Price")
  private double price;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("ShortDescription")
  private String shortDescription;

  @JsonProperty("Total")
  private int total;

  @JsonProperty("BrandId")
  private long brandId;

  @JsonProperty("CategoryIds")
  private Set<Long> setOfCategoryIds;

  @JsonProperty("Attributes")
  private Map<Long, String> mapOfProductAttributes;

  @JsonProperty("Variations")
  private Set<Long> setOfVariationIds;
}
