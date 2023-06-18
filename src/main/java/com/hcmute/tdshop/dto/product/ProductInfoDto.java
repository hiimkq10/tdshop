package com.hcmute.tdshop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.entity.Brand;
import com.hcmute.tdshop.entity.Image;
import com.hcmute.tdshop.entity.ProductStatus;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ProductInfoDto {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Sku")
  private String sku;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("ImageUrl")
  private String imageUrl;

  @JsonProperty("Price")
  private String price;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("ShortDescription")
  private String shortDescription;

  @JsonProperty("Total")
  private int total;

  @JsonProperty("SelAmount")
  private int selAmount;

  @JsonProperty("CreatedAt")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;

  @JsonProperty("DeletedAt")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime deletedAt;

  @JsonProperty("Status")
  private ProductStatus status;

  @JsonProperty("Categories")
  private Set<ProductCategoryDto> setOfCategories;

  @JsonProperty("Brand")
  private Brand brand;

  @JsonProperty("Images")
  private Set<Image> setOfImages;

  @JsonProperty("Attributes")
  private Set<ProductAttributeDto> setOfProductAttributes;

  @JsonProperty("Variations")
  private Set<ProductVariationOptionDto> setOfVariationOptions;

  @JsonProperty("Discount")
  private ProductPromotionDto productPromotion;

  @JsonProperty("MCategoryId")
  private Long mCategoryId;

  @JsonProperty("Length")
  private Double length;

  @JsonProperty("Width")
  private Double width;

  @JsonProperty("Height")
  private Double height;

  @JsonProperty("Weight")
  private Double weight;
}
