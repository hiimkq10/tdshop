package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddProductRequest {
  @JsonProperty("Name")
  @NotBlank(message = ApplicationConstants.PRODUCT_NAME_MANDATORY)
  @Size(max = 100, message = ApplicationConstants.PRODUCT_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("Price")
  @Min(value = 1000, message = ApplicationConstants.PRODUCT_PRICE_INVALID)
  private double price;

  @JsonProperty("Description")
  @NotBlank(message = ApplicationConstants.PRODUCT_DESCRIPTION_MANDATORY)
  @Size(max = 65535, message = ApplicationConstants.PRODUCT_DESCRIPTION_SIZE_INVALID)
  private String description;

  @JsonProperty("ShortDescription")
  @NotBlank(message = ApplicationConstants.PRODUCT_SHORT_DESCRIPTION_MANDATORY)
  @Size(max = 255, message = ApplicationConstants.PRODUCT_SHORT_DESCRIPTION_SIZE_INVALID)
  private String shortDescription;

  @JsonProperty("Total")
  @Min(value = 0, message = ApplicationConstants.PRODUCT_TOTAL_INVALID)
  private int total;

  @JsonProperty("BrandId")
  @Positive(message = ApplicationConstants.BRAND_ID_INVALID)
  private long brandId;

  @JsonProperty("CategoryIds")
  @Size(min = 1, message = ApplicationConstants.PRODUCT_MUST_BELONGS_TO_ONE_CATEGORY)
  private Set<Long> setOfCategoryIds;

  @JsonProperty("Attributes")
  private Map<Long, String> mapOfProductAttributes;

  @JsonProperty("Variations")
  private Set<Long> setOfVariationIds;

  @JsonProperty("MainImage")
  private Map<String, String> mainImage = new HashMap<>();

  @JsonProperty("Images")
  private Map<String, String> images = new HashMap<>();
}
