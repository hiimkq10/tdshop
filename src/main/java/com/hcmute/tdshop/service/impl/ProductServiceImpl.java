package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.dto.product.ProductInfoDto;
import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.Brand;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductAttribute;
import com.hcmute.tdshop.entity.ProductStatus;
import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import com.hcmute.tdshop.mapper.ProductMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AttributeRepository;
import com.hcmute.tdshop.repository.BrandRepository;
import com.hcmute.tdshop.repository.CategoryRepository;
import com.hcmute.tdshop.repository.ProductAttributeRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ProductStatusRepository;
import com.hcmute.tdshop.repository.VariationOptionRepository;
import com.hcmute.tdshop.service.ProductService;
import com.hcmute.tdshop.specification.ProductSpecification;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private AttributeRepository attributeRepository;

  @Autowired
  private VariationOptionRepository variationOptionRepository;

  @Autowired
  private ProductAttributeRepository productAttributeRepository;

  @Autowired
  private ProductStatusRepository productStatusRepository;

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

  @Override
  public DataResponse insertProduct(AddProductRequest request, List<MultipartFile> images) {
    Product product = productMapper.AddProductRequestToProduct(request);
    Optional<Brand> optionalBrand = brandRepository.findById(request.getBrandId());
    if (optionalBrand.isPresent()) {
      Brand brand = optionalBrand.get();
      List<Category> listOfCaregories = categoryRepository.findAllById(request.getSetOfCategoryIds());
      product.setBrand(brand);
      product.setSetOfCategories(new HashSet<>(listOfCaregories));
      product.setSku(UUID.randomUUID().toString());
      product.setImageUrl("http://abc");
      product.setSelAmount(0);
      product.setCreatedAt(LocalDateTime.now());
      product.setStatus(productStatusRepository.findById(ProductStatusEnum.HIDE.getId()).get());
      setProductAttribute(product, request.getMapOfProductAttributes());
      setProductVariation(product, request.getSetOfVariationIds());
      productRepository.save(product);
      return DataResponse.SUCCESSFUL;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.BRAND_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  private void setProductAttribute(Product product, Map<Long, String> mapOfProductAttributes) {
    if (mapOfProductAttributes == null) {
      return;
    }
    List<Attribute> attributes = attributeRepository.findAllById(mapOfProductAttributes.keySet());
    product.setSetOfProductAttributes(new HashSet<>());
    for (Attribute attribute : attributes) {
      product.getSetOfProductAttributes()
          .add(new ProductAttribute(null, mapOfProductAttributes.get(attribute.getId()), attribute, product));
    }
  }

  private void setProductVariation(Product product, Set<Long> setOfVariationIds) {
    if (setOfVariationIds == null) {
      return;
    }
    List<VariationOption> variationOptions = variationOptionRepository.findAllById(setOfVariationIds);
    product.setSetOfVariationOptions(new HashSet<>());
    for (VariationOption variationOption : variationOptions) {
      product.getSetOfVariationOptions().add(variationOption);
    }
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
