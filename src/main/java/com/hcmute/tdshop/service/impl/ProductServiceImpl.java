package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.product.ProductInfoDto;
import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.mapper.ProductMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.service.ProductService;
import com.hcmute.tdshop.specification.ProductSpecification;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductMapper productMapper;

  @Override
  public DataResponse getAllProducts(Pageable page) {
    Page<Product> pageOfProducts = productRepository.findAll(page);
    Page<SimpleProductDto> pageOfSimpleProducts = new PageImpl<SimpleProductDto>(
        pageOfProducts.getContent().stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfSimpleProducts);
  }

  @Override
  public DataResponse searchProductsByFilter(long categoryId, double maxPrice, double minPrice,
      Set<Long> variationOptionIds,
      Pageable page) {
    Specification<Product> conditions = Specification.where(ProductSpecification.isNotDeleted())
        .and(ProductSpecification.isNotHide());
    if (maxPrice > 0) {
      conditions = conditions.and(ProductSpecification.hasPriceLessThanOrEqualTo(maxPrice));
    }
    if (minPrice > 0) {
      conditions = conditions.and(ProductSpecification.hasPriceGreaterThanOrEqualTo(minPrice));
    }

    Page<Product> pageOfProducts = productRepository.findAll(conditions, page);
    List<Product> listOfProducts = pageOfProducts.getContent();

    // Filter by category
    if (categoryId > 0) {
      listOfProducts = listOfProducts.stream().filter(product -> checkIfProductHasCategory(product, categoryId))
          .collect(Collectors.toList());
    }

    // Filter by variation
    if (variationOptionIds != null) {
      listOfProducts = listOfProducts.stream()
          .filter(product -> checkIfProductContainAllVariation(product, variationOptionIds)).collect(
              Collectors.toList());
    }

    Page<SimpleProductDto> pageOfSimpleProducts = new PageImpl<SimpleProductDto>(
        listOfProducts.stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfSimpleProducts);
  }

  @Override
  public DataResponse searchProductsByKeyword(String keyword, Pageable page) {
    Specification<Product> conditions = Specification.where(ProductSpecification.isNotDeleted())
        .and(ProductSpecification.isNotHide());
    if (keyword != null) {
      conditions = conditions.and(ProductSpecification.hasName(keyword));
    }
    Page<Product> pageOfProducts = productRepository.findAll(conditions, page);
    Page<SimpleProductDto> pageOfSimpleProducts = new PageImpl<SimpleProductDto>(
        pageOfProducts.getContent().stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfSimpleProducts);
  }

  @Override
  public DataResponse getProductById(long id) {
    Specification<Product> conditions = Specification.where(ProductSpecification.isNotDeleted())
        .and(ProductSpecification.isNotHide())
        .and(ProductSpecification.hasId(id));
    List<Product> listOfProduct = productRepository.findAll(conditions);
    if (listOfProduct.size() > 0) {
      ProductInfoDto productInfoDto = productMapper.ProductToProductInfoDto(listOfProduct.get(0));
      return new DataResponse(productInfoDto);
    }
    return new DataResponse(ApplicationConstants.NOT_FOUND, ApplicationConstants.PRODUCT_NOT_FOUND,
        ApplicationConstants.NOT_FOUND_CODE);
  }

  private boolean checkIfProductContainAllVariation(Product product, Set<Long> variationOptionIds) {
    Set<Long> setOfIds = product.getSetOfVariationOptions().stream().map(VariationOption::getId)
        .collect(Collectors.toSet());
    return setOfIds.containsAll(variationOptionIds);
  }

  private boolean checkIfProductHasCategory(Product product, long categoryId) {
    Set<Long> setOfIds = product.getSetOfCategories().stream().map(Category::getId)
        .collect(Collectors.toSet());
    return setOfIds.contains(categoryId);
  }
}
