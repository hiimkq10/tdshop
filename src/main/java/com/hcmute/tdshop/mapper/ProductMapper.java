package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.dto.product.ProductAttributeDto;
import com.hcmute.tdshop.dto.product.ProductCategoryDto;
import com.hcmute.tdshop.dto.product.ProductInfoDto;
import com.hcmute.tdshop.dto.product.ProductPromotionDto;
import com.hcmute.tdshop.dto.product.ProductVariationOptionDto;
import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.dto.product.UpdateProductRequest;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Image;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductAttribute;
import com.hcmute.tdshop.entity.ProductPromotion;
import com.hcmute.tdshop.entity.VariationOption;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.Set;
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
    simpleProductDto.setProductPromotion( ProductPromotionToProductPromotionDto( product.getProductPromotion() ) );

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
    productInfoDto.setSetOfVariationOptions( variationOptionSetToProductVariationOptionDtoSet( product.getSetOfVariationOptions() ) );
    productInfoDto.setProductPromotion( ProductPromotionToProductPromotionDto( product.getProductPromotion() ) );

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
}
