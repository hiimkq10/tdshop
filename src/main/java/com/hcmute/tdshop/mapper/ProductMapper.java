package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.dto.product.ProductAttributeDto;
import com.hcmute.tdshop.dto.product.ProductCategoryDto;
import com.hcmute.tdshop.dto.product.ProductInfoDto;
import com.hcmute.tdshop.dto.product.ProductPromotionDto;
import com.hcmute.tdshop.dto.product.ProductVariationOptionDto;
import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.dto.product.UpdateProductRequest;
import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.AttributeSet;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Image;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductAttribute;
import com.hcmute.tdshop.entity.ProductPromotion;
import com.hcmute.tdshop.entity.VariationOption;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

  public SimpleProductDto ProductToSimpleProductDto(Product product) {
    if ( product == null ) {
      return null;
    }

    SimpleProductDto simpleProductDto = new SimpleProductDto();

    simpleProductDto.setId( product.getId() );
    simpleProductDto.setSku( product.getSku() );
    simpleProductDto.setName( product.getName() );
    simpleProductDto.setPrice( new BigDecimal(product.getPrice()).toPlainString() );
    simpleProductDto.setImageUrl( product.getImageUrl() );
    simpleProductDto.setBrand(product.getBrand());
    simpleProductDto.setSelAmount(product.getSelAmount());
    simpleProductDto.setProductPromotion( ProductPromotionToProductPromotionDto( getCurrentPromotion(product) ) );

    return simpleProductDto;
  }

  public ProductInfoDto ProductToProductInfoDto(Product product) {
    if ( product == null ) {
      return null;
    }

    ProductInfoDto productInfoDto = new ProductInfoDto();

    productInfoDto.setId( product.getId() );
    productInfoDto.setSku( product.getSku() );
    productInfoDto.setName( product.getName() );
    productInfoDto.setImageUrl( product.getImageUrl() );
    productInfoDto.setPrice( new BigDecimal(product.getPrice()).toPlainString() );
    productInfoDto.setDescription( product.getDescription() );
    productInfoDto.setShortDescription( product.getShortDescription() );
    productInfoDto.setTotal( product.getTotal() );
    productInfoDto.setSelAmount( product.getSelAmount() );
    productInfoDto.setCreatedAt( product.getCreatedAt() );
    productInfoDto.setDeletedAt( product.getDeletedAt() );
    productInfoDto.setStatus( product.getStatus() );
    productInfoDto.setSetOfCategories( categorySetToProductCategoryDtoSet( product.getSetOfCategories() ) );
    productInfoDto.setBrand( product.getBrand() );
    Set<Image> set1 = product.getSetOfImages();
    if ( set1 != null ) {
      productInfoDto.setSetOfImages( new LinkedHashSet<Image>( set1 ) );
    }
    productInfoDto.setSetOfProductAttributes( productAttributeSetToProductAttributeDtoSet( product.getSetOfProductAttributes() ) );

    if (product.getSetOfProductAttributes().size() > 0) {
      if (productInfoDto.getSetOfProductAttributes() == null) {
        productInfoDto.setSetOfProductAttributes(new HashSet<>());
      }
      ProductAttribute productAttribute = product.getSetOfProductAttributes().stream().findFirst().get();
      AttributeSet attributeSet = productAttribute.getAttribute().getAttributeSet();
      productInfoDto.setAttributeSetId(attributeSet.getId());
      productInfoDto.setAttributeSetName(attributeSet.getName());
      List<Long> ids = productInfoDto.getSetOfProductAttributes().stream().map(item -> item.getAttributeId()).collect(
          Collectors.toList());
      for (Attribute attribute : attributeSet.getSetOfAttributes()) {
        if (!ids.contains(attribute.getId())) {
          productInfoDto.getSetOfProductAttributes().add(new ProductAttributeDto(0, attribute.getName(), "", attribute.getPriority(), attribute.getId()));
        }
      }
    }

    productInfoDto.setSetOfVariationOptions( variationOptionSetToProductVariationOptionDtoSet( product.getSetOfVariationOptions() ) );
    productInfoDto.setProductPromotion( ProductPromotionToProductPromotionDto( getCurrentPromotion(product) ) );
    if (product.getSetOfCategories() != null && product.getSetOfCategories().size() > 0) {
      productInfoDto.setMCategoryId(product.getSetOfCategories().iterator().next().getMasterCategory().getId());
    }
    productInfoDto.setLength(product.getLength());
    productInfoDto.setWidth(product.getWidth());
    productInfoDto.setHeight(product.getHeight());
    productInfoDto.setWeight(product.getWeight());

    return productInfoDto;
  }

  public abstract ProductCategoryDto CategoryToProductCategoryDto(Category category);

  public ProductAttributeDto AttributeToProductAttributeDto(ProductAttribute attribute) {
    if (attribute == null) {
      return null;
    }

    ProductAttributeDto productAttributeDto = new ProductAttributeDto();
    productAttributeDto.setId(attribute.getId());
    productAttributeDto.setName(attribute.getAttribute().getName());
    productAttributeDto.setValue(attribute.getValue());
    productAttributeDto.setPriority(attribute.getAttribute().getPriority());
    productAttributeDto.setAttributeId(attribute.getAttribute().getId());

    return productAttributeDto;
  }

  public ProductVariationOptionDto VariationOptionToProductVariationOptionDto(VariationOption variationOption) {
    if (variationOption == null) {
      return null;
    }
    ProductVariationOptionDto productVariationOptionDto = new ProductVariationOptionDto();
    productVariationOptionDto.setId(variationOption.getId());
    productVariationOptionDto.setName(variationOption.getVariation().getName());
    productVariationOptionDto.setValue(variationOption.getValue());

    return productVariationOptionDto;
  }

  public abstract ProductPromotionDto ProductPromotionToProductPromotionDto(ProductPromotion productPromotion);

  public abstract Product AddProductRequestToProduct(AddProductRequest request);

  public abstract Product UpdateProductRequestToProduct(UpdateProductRequest request);

  public abstract Set<ProductCategoryDto> categorySetToProductCategoryDtoSet(Set<Category> set);

  public abstract Set<ProductAttributeDto> productAttributeSetToProductAttributeDtoSet(Set<ProductAttribute> set);

  public abstract Set<ProductVariationOptionDto> variationOptionSetToProductVariationOptionDtoSet(Set<VariationOption> set);

  private ProductPromotion getCurrentPromotion(Product product) {
    if (product.getSetOfProductPromotions() == null) {
      return null;
    }
    ProductPromotion promotion = null;
    LocalDateTime now = LocalDateTime.now();
    for (ProductPromotion productPromotion : product.getSetOfProductPromotions()) {
      if (isBeforeOrEqual(productPromotion.getStartDate(), now) && isAfterOrEqual(productPromotion.getEndDate(), now)) {
        promotion = productPromotion;
        break;
      }
    }
    return promotion;
  }

  private boolean isBeforeOrEqual(LocalDateTime date1, LocalDateTime date2){
    return date1.isBefore(date2) || date1.isEqual(date2);
  }

  private boolean isAfterOrEqual(LocalDateTime date1, LocalDateTime date2){
    return date1.isAfter(date2) || date1.isEqual(date2);
  }
}
