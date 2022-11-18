package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
  public abstract SimpleProductDto ProductToSimpleProductDto(Product product);
}
