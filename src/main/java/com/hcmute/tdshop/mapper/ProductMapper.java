package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.product.ProductAttributeDto;
import com.hcmute.tdshop.dto.product.ProductCategoryDto;
import com.hcmute.tdshop.dto.product.ProductInfoDto;
import com.hcmute.tdshop.dto.product.ProductPromotionDto;
import com.hcmute.tdshop.dto.product.ProductVariationOptionDto;
import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductAttribute;
import com.hcmute.tdshop.entity.ProductPromotion;
import com.hcmute.tdshop.entity.VariationOption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

  public abstract SimpleProductDto ProductToSimpleProductDto(Product product);

  public abstract ProductInfoDto ProductToProductInfoDto(Product product);

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
}
