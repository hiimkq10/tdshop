package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {

  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.PRODUCT_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("Price")
  private double price;

  @JsonProperty("Description")
  @Size(max = 65535, message = ApplicationConstants.PRODUCT_DESCRIPTION_SIZE_INVALID)
  private String description;

  @JsonProperty("ShortDescription")
  @Size(max = 255, message = ApplicationConstants.PRODUCT_SHORT_DESCRIPTION_SIZE_INVALID)
  private String shortDescription;

  @JsonProperty("Total")
  private int total;

  @JsonProperty("BrandId")
  private long brandId;

  @JsonProperty("Status")
  private long productStatus;

  @JsonProperty("CategoryIds")
  private Set<Long> setOfCategoryIds;

  @JsonProperty("Attributes")
  private Map<Long, String> mapOfProductAttributes;

  @JsonProperty("Variations")
  private Set<Long> setOfVariationIds;

  @JsonProperty("Length")
  private Double length = 0.0;

  @JsonProperty("Width")
  private Double width = 0.0;

  @JsonProperty("Height")
  private Double height = 0.0;

  @JsonProperty("Weight")
  private Double weight = 0.0;

  @JsonProperty("DeletedImages")
  private List<String> listOfDeletedImageUrls = new ArrayList<>();

  @JsonProperty("MainImage")
  private Map<String, String> mainImage = new HashMap<>();

  @JsonProperty("Images")
  private Map<String, String> images = new HashMap<>();
}
