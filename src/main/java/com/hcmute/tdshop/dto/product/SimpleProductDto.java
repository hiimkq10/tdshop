package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.dto.category.CategoryDto;
import com.hcmute.tdshop.dto.variationoption.VariationOptionDto;
import com.hcmute.tdshop.entity.Brand;
import com.hcmute.tdshop.entity.MasterCategory;
import com.hcmute.tdshop.entity.VariationOption;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class SimpleProductDto {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Sku")
  private String sku;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Price")
  private String price;

  @JsonProperty("ImageUrl")
  private String imageUrl;

  @JsonProperty("Brand")
  private Brand brand;

  @JsonProperty("Total")
  private int total;

  @JsonProperty("SelAmount")
  private int selAmount;

  @JsonProperty("Discount")
  private ProductPromotionDto productPromotion;

  @JsonProperty("MCategory")
  private MasterCategory mCategory;

  @JsonProperty("Categories")
  private List<CategoryDto> categories;

  @JsonProperty("Variations")
  private Set<ProductVariationOptionDto> setOfVariationOptions;
}
