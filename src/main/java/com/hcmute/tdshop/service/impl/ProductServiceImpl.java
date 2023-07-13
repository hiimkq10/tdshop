package com.hcmute.tdshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.dto.product.ChangeProductStatusRequest;
import com.hcmute.tdshop.dto.product.ProductInfoDto;
import com.hcmute.tdshop.dto.product.RecommendProductResponse;
import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.dto.product.UpdateProductRequest;
import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.Brand;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Image;
import com.hcmute.tdshop.entity.Notification;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductAttribute;
import com.hcmute.tdshop.entity.ProductStatus;
import com.hcmute.tdshop.entity.UserClick;
import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import com.hcmute.tdshop.mapper.ProductMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AttributeRepository;
import com.hcmute.tdshop.repository.BrandRepository;
import com.hcmute.tdshop.repository.CategoryRepository;
import com.hcmute.tdshop.repository.ImageRepository;
import com.hcmute.tdshop.repository.NotificationRepository;
import com.hcmute.tdshop.repository.ProductAttributeRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ProductStatusRepository;
import com.hcmute.tdshop.repository.UserClickRepository;
import com.hcmute.tdshop.repository.VariationOptionRepository;
import com.hcmute.tdshop.service.ProductService;
import com.hcmute.tdshop.specification.ProductSpecification;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import com.hcmute.tdshop.utils.notification.NotificationHelper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

  Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

  //  @Value("${CLOUDINARY_URL}")
  private String cloudinaryURL = "cloudinary://484587933643945:LZyMCt-jqVo9hbKSkH3JT2tn0oo@dd4dftliy";

  //  @Value("${cloudinaryProductImagePath}")
  private String imagePath = "tdshop/product/images";

  @Value("${app.click-history.allowed-range-in-day}")
  private int clickRangeInDay;

  private Cloudinary cloudinary;

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

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private NotificationHelper notificationHelper;

  @Autowired
  private UserClickRepository userClickRepository;

  @Override
  public DataResponse getAllProducts(Pageable page) {
    List<Specification<Product>> specifications = createSpecificationsBaseOnLoggedInUser();
    Specification<Product> conditions = SpecificationHelper.and(specifications);
    Page<Product> pageOfProducts = productRepository.findAll(conditions, page);
    Page<SimpleProductDto> pageOfSimpleProducts = new PageImpl<SimpleProductDto>(
        pageOfProducts.getContent().stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfSimpleProducts);
  }

  @Override
  public DataResponse searchProductsByFilter(String keyword, long categoryId, double maxPrice, double minPrice,
      String brand,
      Long brandId,
      Set<Long> variationOptionIds,
      long masterCategoryId,
      Pageable page) {
    List<Specification<Product>> specifications = createSpecificationsBaseOnLoggedInUser();
    if (keyword != null) {
      List<Specification<Product>> ors = new ArrayList<>();
      ors.add(ProductSpecification.hasSku(keyword));
      ors.add(ProductSpecification.hasName(keyword));
      ors.add(ProductSpecification.hasBrand(keyword));
      specifications.add(SpecificationHelper.or(ors));
    }

    // Filter by variation
    if (variationOptionIds != null) {
      specifications.add(ProductSpecification.hasVariations(variationOptionIds));
    }

    // Filter by category
    if (categoryId > 0) {
      specifications.add(ProductSpecification.hasCategory(categoryId));
    }

    if (maxPrice > 0) {
      specifications.add(ProductSpecification.hasPriceLessThanOrEqualTo(maxPrice));
    }
    if (minPrice > 0) {
      specifications.add(ProductSpecification.hasPriceGreaterThanOrEqualTo(minPrice));
    }

    if (brand != null) {
      specifications.add(ProductSpecification.hasBrand(brand));
    }

    if (brandId > 0) {
      specifications.add(ProductSpecification.hasBrandId(brandId));
    }

    // Filter by mastercategory
    if (masterCategoryId > 0) {
      specifications.add(ProductSpecification.hasMasterCategory(masterCategoryId));
    }

    Specification<Product> conditions = SpecificationHelper.and(specifications);
    Page<Product> pageOfProducts = productRepository.findAll(conditions, page);
    List<Product> listOfProducts = pageOfProducts.getContent();

    // Filter by category
//    if (categoryId > 0) {
//      listOfProducts = listOfProducts.stream().filter(product -> checkIfProductHasCategory(product, categoryId))
//          .collect(Collectors.toList());
//    }

    // Filter by variation
//    if (variationOptionIds != null) {
//      listOfProducts = listOfProducts.stream()
//          .filter(product -> checkIfProductContainAllVariation(product, variationOptionIds)).collect(
//              Collectors.toList());
//    }
    for (Product p : pageOfProducts.getContent()) {
      System.out.println(p.getCurrentDiscountRate());
    }
    Page<SimpleProductDto> pageOfSimpleProducts = new PageImpl<SimpleProductDto>(
        listOfProducts.stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfSimpleProducts);
  }

  public Specification<Product> buildCriteria(String keyword, long categoryId, double maxPrice, double minPrice,
      String brand, Long brandId, Set<Long> variationOptionIds) {
    List<Specification<Product>> specifications = new ArrayList<>();
    specifications.add(ProductSpecification.isNotDeleted());
    if (keyword != null) {
      List<Specification<Product>> ors = new ArrayList<>();
      ors.add(ProductSpecification.hasSku(keyword));
      ors.add(ProductSpecification.hasName(keyword));
      ors.add(ProductSpecification.hasBrand(keyword));
      specifications.add(SpecificationHelper.or(ors));
    }

    // Filter by variation
    if (variationOptionIds != null) {
      specifications.add(ProductSpecification.hasVariations(variationOptionIds));
    }

    // Filter by category
    if (categoryId > 0) {
      specifications.add(ProductSpecification.hasCategory(categoryId));
    }

    if (maxPrice > 0) {
      specifications.add(ProductSpecification.hasPriceLessThanOrEqualTo(maxPrice));
    }
    if (minPrice > 0) {
      specifications.add(ProductSpecification.hasPriceGreaterThanOrEqualTo(minPrice));
    }

    if (brand != null) {
      specifications.add(ProductSpecification.hasBrand(brand));
    }

    if (brandId > 0) {
      specifications.add(ProductSpecification.hasBrandId(brandId));
    }

    Specification<Product> conditions = SpecificationHelper.and(specifications);
    return conditions;
  }

  @Override
  public DataResponse searchProductsByFilterForAdmin(String keyword, long categoryId, double maxPrice, double minPrice,
      String brand, Long brandId, Set<Long> variationOptionIds, Pageable page) {
    Specification<Product> conditions = buildCriteria(keyword, categoryId, maxPrice, minPrice, brand, brandId,
        variationOptionIds);
    Page<Product> pageOfProducts = productRepository.findAll(conditions, page);
    List<Product> listOfProducts = pageOfProducts.getContent();

    Page<SimpleProductDto> pageOfSimpleProducts = new PageImpl<SimpleProductDto>(
        listOfProducts.stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfSimpleProducts);
  }

  @Override
  public DataResponse getProductsByFilterForAdmin(String keyword, long categoryId, double maxPrice, double minPrice,
      String brand, Long brandId, Set<Long> variationOptionIds, Pageable page) {
    Specification<Product> conditions = buildCriteria(keyword, categoryId, maxPrice, minPrice, brand, brandId,
        variationOptionIds);
    Page<Product> pageOfProducts = productRepository.findAll(conditions, page);
    List<Product> listOfProducts = pageOfProducts.getContent();

    Page<ProductInfoDto> pageOfProductInfos = new PageImpl<ProductInfoDto>(
        listOfProducts.stream().map(productMapper::ProductToProductInfoDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfProductInfos);
  }

  @Override
  public DataResponse searchProductsByKeyword(String keyword, Pageable page) {
    List<Specification<Product>> specifications = createSpecificationsBaseOnLoggedInUser();
    if (keyword != null) {
      List<Specification<Product>> ors = new ArrayList<>();
      ors.add(ProductSpecification.hasSku(keyword));
      ors.add(ProductSpecification.hasName(keyword));
      ors.add(ProductSpecification.hasBrand(keyword));
      specifications.add(SpecificationHelper.or(ors));
    }
    Specification<Product> conditions = SpecificationHelper.and(specifications);
    Page<Product> pageOfProducts = productRepository.findAll(conditions, page);
    Page<SimpleProductDto> pageOfSimpleProducts = new PageImpl<SimpleProductDto>(
        pageOfProducts.getContent().stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()),
        page,
        pageOfProducts.getTotalElements()
    );
    return new DataResponse(pageOfSimpleProducts);
  }

  @Override
  public DataResponse getProductByIdForAdmin(long id) {
    List<Specification<Product>> specifications = new ArrayList<>();
    specifications.add(ProductSpecification.isNotDeleted());
    specifications.add(ProductSpecification.hasId(id));
    Specification<Product> conditions = SpecificationHelper.and(specifications);
    List<Product> listOfProduct = productRepository.findAll(conditions);
    if (listOfProduct.size() > 0) {
      ProductInfoDto productInfoDto = productMapper.ProductToProductInfoDto(listOfProduct.get(0));
      return new DataResponse(productInfoDto);
    }
    return new DataResponse(ApplicationConstants.NOT_FOUND, ApplicationConstants.PRODUCT_NOT_FOUND,
        ApplicationConstants.NOT_FOUND_CODE);
  }

  @Override
  public DataResponse getProductById(long id) {
    List<Specification<Product>> specifications = createSpecificationsBaseOnLoggedInUser();
    specifications.add(ProductSpecification.hasId(id));
    Specification<Product> conditions = SpecificationHelper.and(specifications);
    List<Product> listOfProduct = productRepository.findAll(conditions);
    if (listOfProduct.size() > 0) {
      ProductInfoDto productInfoDto = productMapper.ProductToProductInfoDto(listOfProduct.get(0));
      return new DataResponse(productInfoDto);
    }
    return new DataResponse(ApplicationConstants.NOT_FOUND, ApplicationConstants.PRODUCT_NOT_FOUND,
        ApplicationConstants.NOT_FOUND_CODE);
  }

  @Override
  @Transactional
  public DataResponse insertProduct(AddProductRequest request, MultipartFile mainImage, List<MultipartFile> images) {
    Product product = productMapper.AddProductRequestToProduct(request);
    Optional<Brand> optionalBrand = brandRepository.findById(request.getBrandId());
    if (optionalBrand.isPresent()) {
      Brand brand = optionalBrand.get();
      List<Category> listOfCaregories = categoryRepository.findAllById(request.getSetOfCategoryIds());
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd");
      product.setBrand(brand);
      product.setSku(dateTimeFormatter.format(LocalDateTime.now()) + generateRandom(8));
      product.setSelAmount(0);
      product.setCreatedAt(LocalDateTime.now());
      product.setStatus(productStatusRepository.findById(ProductStatusEnum.HIDE.getId()).get());

      // Add Category
      product.setSetOfCategories(new HashSet<>(listOfCaregories));
      for (Category c : listOfCaregories) {
        c.getSetOfProducts().add(product);
      }

      // Upload first image
      String imageUrl = uploadProductImage(mainImage);
      if (imageUrl == null) {
        return new DataResponse(ApplicationConstants.FAILED, ApplicationConstants.IMAGE_UPLOAD_FAILED,
            ApplicationConstants.FAILED_CODE);
      }
      product.setImageUrl(imageUrl);

      // Add Attribute
      Map<Long, String> mapOfProductAttributes = request.getMapOfProductAttributes();
      if (mapOfProductAttributes != null) {
        List<Attribute> attributes = attributeRepository.findAllById(mapOfProductAttributes.keySet());
        product.setSetOfProductAttributes(new HashSet<>());
        for (Attribute attribute : attributes) {
          product.getSetOfProductAttributes()
              .add(new ProductAttribute(null, mapOfProductAttributes.get(attribute.getId()), attribute, product));
        }
      }

      // Add Variation
      Set<Long> setOfVariationIds = request.getSetOfVariationIds();
      if (setOfVariationIds != null) {
        List<VariationOption> variationOptions = variationOptionRepository.findAllById(setOfVariationIds);
        product.setSetOfVariationOptions(new HashSet<>());
        for (VariationOption variationOption : variationOptions) {
          product.getSetOfVariationOptions().add(variationOption);
        }
      }

      String url;
      product.setSetOfImages(new HashSet<>());
      if (images != null) {
        for (int i = 0; i < images.size(); i++) {
          MultipartFile image = images.get(i);
          url = uploadProductImage(image);
          if (url != null) {
            product.getSetOfImages().add(new Image(null, url, null, product));
          }
        }
      }

      product = productRepository.save(product);
      Notification notification = notificationHelper.buildNewProductAddedNotification(product);
      notificationRepository.save(notification);

      // Upload the rest image to server
//      if (images.size() > 0) {
//        product.setSetOfImages(new HashSet<>());
//        Product finalProduct = product;
//        Thread thread = new Thread(new Runnable() {
//          @Override
//          public void run() {
//            String url;
//            for (int i = 0; i < images.size(); i++) {
//              MultipartFile image = images.get(i);
//              url = uploadProductImage(image);
//              System.out.println("test: " + url);
//              if (url != null) {
//                finalProduct.getSetOfImages().add(new Image(null, url, null, finalProduct));
//              }
//            }
//            productRepository.saveAndFlush(finalProduct);
//          }
//        });
//        thread.start();
//      }

      return new DataResponse(ApplicationConstants.PRODUCT_ADD_SUCCESSFULLY,
          productMapper.ProductToProductInfoDto(product));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.BRAND_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

//  @Override
//  @Transactional
//  public DataResponse insertProduct(AddProductRequest request) {
////    mainImage, images
//    Map<String, String> mainImage = request.getMainImage();
//    Map<String, String> images = request.getImages();
//    Product product = productMapper.AddProductRequestToProduct(request);
//    Optional<Brand> optionalBrand = brandRepository.findById(request.getBrandId());
//    if (optionalBrand.isPresent()) {
//      Brand brand = optionalBrand.get();
//      List<Category> listOfCaregories = categoryRepository.findAllById(request.getSetOfCategoryIds());
//      product.setBrand(brand);
//      product.setSku(UUID.randomUUID().toString());
//      product.setSelAmount(0);
//      product.setCreatedAt(LocalDateTime.now());
//      product.setStatus(productStatusRepository.findById(ProductStatusEnum.HIDE.getId()).get());
//
//      // Add Category
//      product.setSetOfCategories(new HashSet<>(listOfCaregories));
//      for (Category c : listOfCaregories) {
//        c.getSetOfProducts().add(product);
//      }
//
//      // Upload first image
//      String imageUrl = null;
//      for (String key : mainImage.keySet()) {
//        imageUrl = uploadProductImage(mainImage.get(key), key);
//      }
//      if (Objects.isNull(imageUrl)) {
//        return new DataResponse(ApplicationConstants.FAILED, ApplicationConstants.IMAGE_UPLOAD_FAILED,
//            ApplicationConstants.FAILED_CODE);
//      }
//      product.setImageUrl(imageUrl);
//
//      // Add Attribute
//      Map<Long, String> mapOfProductAttributes = request.getMapOfProductAttributes();
//      if (mapOfProductAttributes != null) {
//        List<Attribute> attributes = attributeRepository.findAllById(mapOfProductAttributes.keySet());
//        product.setSetOfProductAttributes(new HashSet<>());
//        for (Attribute attribute : attributes) {
//          product.getSetOfProductAttributes()
//              .add(new ProductAttribute(null, mapOfProductAttributes.get(attribute.getId()), attribute, product));
//        }
//      }
//
//      // Add Variation
//      Set<Long> setOfVariationIds = request.getSetOfVariationIds();
//      if (setOfVariationIds != null) {
//        List<VariationOption> variationOptions = variationOptionRepository.findAllById(setOfVariationIds);
//        product.setSetOfVariationOptions(new HashSet<>());
//        for (VariationOption variationOption : variationOptions) {
//          product.getSetOfVariationOptions().add(variationOption);
//        }
//      }
//
//      String url;
//      product.setSetOfImages(new HashSet<>());
//      for (String key : images.keySet()) {
//        url = uploadProductImage(images.get(key), key);
//        if (url != null) {
//          product.getSetOfImages().add(new Image(null, url, key, product));
//        }
//      }
////      for (int i = 0; i < images.size(); i++) {
////        MultipartFile image = images.get(i);
////        url = uploadProductImage(image);
////        if (url != null) {
////          product.getSetOfImages().add(new Image(null, url, null, product));
////        }
////      }
//
//      product = productRepository.save(product);
//
//      return new DataResponse(ApplicationConstants.PRODUCT_ADD_SUCCESSFULLY,
//          productMapper.ProductToProductInfoDto(product));
//    }
//    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.BRAND_NOT_FOUND,
//        ApplicationConstants.BAD_REQUEST_CODE);
//  }

  @Override
  @Transactional
  public DataResponse updateProduct(long id, UpdateProductRequest request, MultipartFile mainImage,
      List<MultipartFile> images) {
    Product productToUpdate = productMapper.UpdateProductRequestToProduct(request);
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (optionalProduct.isPresent()) {
      Product currentProduct = optionalProduct.get();
      if (productToUpdate.getName() != null) {
        currentProduct.setName(productToUpdate.getName());
      }
      if (productToUpdate.getPrice() > 1000) {
        currentProduct.setPrice(productToUpdate.getPrice());
      }
      currentProduct.setDescription(productToUpdate.getDescription());
      currentProduct.setShortDescription(productToUpdate.getShortDescription());
      if (productToUpdate.getTotal() >= 0) {
        currentProduct.setTotal(productToUpdate.getTotal());
      }

      if (productToUpdate.getLength() > 0) {
        currentProduct.setLength(productToUpdate.getLength());
      }

      if (productToUpdate.getWidth() > 0) {
        currentProduct.setWidth(productToUpdate.getWidth());
      }

      if (productToUpdate.getHeight() > 0) {
        currentProduct.setHeight(productToUpdate.getHeight());
      }

      if (productToUpdate.getWeight() > 0) {
        currentProduct.setWeight(productToUpdate.getWeight());
      }

      Optional<Brand> optionalBrand = brandRepository.findById(request.getBrandId());
      currentProduct.setBrand(optionalBrand.orElse(null));

      if (request.getProductStatus() > 0) {
        Optional<ProductStatus> optionalProductStatus = productStatusRepository.findById(request.getProductStatus());
        optionalProductStatus.ifPresent(currentProduct::setStatus);
      }

      // Update categories
      Set<Long> categoryIds = request.getSetOfCategoryIds();
      Iterator<Category> categoryIterator = currentProduct.getSetOfCategories().iterator();
      Category category;
      while (categoryIterator.hasNext()) {
        category = categoryIterator.next();
        if (categoryIds.contains(category.getId())) {
          categoryIds.remove(category.getId());
        } else {
          categoryIterator.remove();
        }
      }
      if (categoryIds.size() > 0) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        currentProduct.getSetOfCategories().addAll(categories);
      }

      // Update attributes
      Map<Long, String> mapOfProductAttributes = request.getMapOfProductAttributes();
      Set<Long> attributeIds = mapOfProductAttributes.keySet();
      List<ProductAttribute> deletedAttributes = new ArrayList<>();
      Iterator<ProductAttribute> productAttributeIterator = currentProduct.getSetOfProductAttributes().iterator();
      ProductAttribute productAttribute;
      while (productAttributeIterator.hasNext()) {
        productAttribute = productAttributeIterator.next();
        if (attributeIds.contains(productAttribute.getAttribute().getId())) {
          productAttribute.setValue(mapOfProductAttributes.get(productAttribute.getAttribute().getId()));
          attributeIds.remove(productAttribute.getAttribute().getId());
        } else {
          deletedAttributes.add(productAttribute);
        }
      }
      if (attributeIds.size() > 0) {
        List<Attribute> attributes = attributeRepository.findAllById(attributeIds);
        for (Attribute attribute : attributes) {
          currentProduct.getSetOfProductAttributes()
              .add(new ProductAttribute(
                  null,
                  mapOfProductAttributes.get(attribute.getId()),
                  attribute,
                  currentProduct
              ));
        }
      }
      productAttributeRepository.deleteAll(deletedAttributes);

      // Update variation option
      Set<Long> variationOptionIds = request.getSetOfVariationIds();
      Iterator<VariationOption> variationOptionIterator = currentProduct.getSetOfVariationOptions().iterator();
      VariationOption variationOption;
      while (variationOptionIterator.hasNext()) {
        variationOption = variationOptionIterator.next();
        if (variationOptionIds.contains(variationOption.getId())) {
          variationOptionIds.remove(variationOption.getId());
        } else {
          variationOptionIterator.remove();
        }
      }
      if (variationOptionIds.size() > 0) {
        List<VariationOption> variationOptions = variationOptionRepository.findAllById(variationOptionIds);
        currentProduct.getSetOfVariationOptions().addAll(variationOptions);
      }

      // Handler update image
      List<String> listOfDeletedImageUrls = request.getListOfDeletedImageUrls();
      List<Image> tempList = new ArrayList<>();
      currentProduct.getSetOfImages().forEach(image -> {
        if (listOfDeletedImageUrls.contains(image.getUrl())) {
          tempList.add(image);
        }
      });
      currentProduct.getSetOfImages().removeIf(image -> listOfDeletedImageUrls.contains(image.getUrl()));
      imageRepository.deleteAll(tempList);

      if (mainImage != null) {
        String url = updateProductImage(mainImage, currentProduct.getImageUrl());
        if (url != null) {
          currentProduct.setImageUrl(url);
        }
      }
      if (images != null) {
        String url;
        for (MultipartFile image : images) {
          url = uploadProductImage(image);
          if (url != null) {
            currentProduct.getSetOfImages().add(new Image(null, url, null, currentProduct));
          }
        }
      }

      currentProduct = productRepository.saveAndFlush(currentProduct);

//      Product finalProduct = currentProduct;
//      Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//          if (mainImage != null) {
//            updateProductImage(mainImage, finalProduct.getImageUrl());
//          }
//          String url;
//          for (MultipartFile image : images) {
//            url = uploadProductImage(image);
//            if (url != null) {
//              finalProduct.getSetOfImages().add(new Image(null, url, null, finalProduct));
//            }
//          }
//          productRepository.saveAndFlush(finalProduct);
//        }
//      });
//      thread.start();

      return new DataResponse(ApplicationConstants.PRODUCT_UPDATE_SUCCESSFULLY,
          productMapper.ProductToProductInfoDto(currentProduct));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

//  @Override
//  @Transactional
//  public DataResponse updateProduct(long id, UpdateProductRequest request) {
//    Map<String, String> mainImage = request.getMainImage();
//    Map<String, String> images = request.getImages();
//    Product productToUpdate = productMapper.UpdateProductRequestToProduct(request);
//    Optional<Product> optionalProduct = productRepository.findById(id);
//    if (optionalProduct.isPresent()) {
//      Product currentProduct = optionalProduct.get();
//      if (productToUpdate.getName() != null) {
//        currentProduct.setName(productToUpdate.getName());
//      }
//      if (productToUpdate.getPrice() > 1000) {
//        currentProduct.setPrice(productToUpdate.getPrice());
//      }
//      currentProduct.setDescription(productToUpdate.getDescription());
//      currentProduct.setShortDescription(productToUpdate.getShortDescription());
//      if (productToUpdate.getTotal() >= 0) {
//        currentProduct.setTotal(productToUpdate.getTotal());
//      }
//
//      Optional<Brand> optionalBrand = brandRepository.findById(request.getBrandId());
//      currentProduct.setBrand(optionalBrand.orElse(null));
//
//      if (request.getProductStatus() > 0) {
//        Optional<ProductStatus> optionalProductStatus = productStatusRepository.findById(request.getProductStatus());
//        optionalProductStatus.ifPresent(currentProduct::setStatus);
//      }
//
//      // Update categories
//      Set<Long> categoryIds = request.getSetOfCategoryIds();
//      Iterator<Category> categoryIterator = currentProduct.getSetOfCategories().iterator();
//      Category category;
//      while (categoryIterator.hasNext()) {
//        category = categoryIterator.next();
//        if (categoryIds.contains(category.getId())) {
//          categoryIds.remove(category.getId());
//        } else {
//          categoryIterator.remove();
//        }
//      }
//      if (categoryIds.size() > 0) {
//        List<Category> categories = categoryRepository.findAllById(categoryIds);
//        currentProduct.getSetOfCategories().addAll(categories);
//      }
//
//      // Update attributes
//      Map<Long, String> mapOfProductAttributes = request.getMapOfProductAttributes();
//      Set<Long> attributeIds = mapOfProductAttributes.keySet();
//      List<ProductAttribute> deletedAttributes = new ArrayList<>();
//      Iterator<ProductAttribute> productAttributeIterator = currentProduct.getSetOfProductAttributes().iterator();
//      ProductAttribute productAttribute;
//      while (productAttributeIterator.hasNext()) {
//        productAttribute = productAttributeIterator.next();
//        if (attributeIds.contains(productAttribute.getAttribute().getId())) {
//          productAttribute.setValue(mapOfProductAttributes.get(productAttribute.getAttribute().getId()));
//          attributeIds.remove(productAttribute.getAttribute().getId());
//        } else {
//          deletedAttributes.add(productAttribute);
//        }
//      }
//      if (attributeIds.size() > 0) {
//        List<Attribute> attributes = attributeRepository.findAllById(attributeIds);
//        for (Attribute attribute : attributes) {
//          currentProduct.getSetOfProductAttributes()
//              .add(new ProductAttribute(
//                  null,
//                  mapOfProductAttributes.get(attribute.getId()),
//                  attribute,
//                  currentProduct
//              ));
//        }
//      }
//      productAttributeRepository.deleteAll(deletedAttributes);
//
//      // Update variation option
//      Set<Long> variationOptionIds = request.getSetOfVariationIds();
//      Iterator<VariationOption> variationOptionIterator = currentProduct.getSetOfVariationOptions().iterator();
//      VariationOption variationOption;
//      while (variationOptionIterator.hasNext()) {
//        variationOption = variationOptionIterator.next();
//        if (variationOptionIds.contains(variationOption.getId())) {
//          variationOptionIds.remove(variationOption.getId());
//        } else {
//          variationOptionIterator.remove();
//        }
//      }
//      if (variationOptionIds.size() > 0) {
//        List<VariationOption> variationOptions = variationOptionRepository.findAllById(variationOptionIds);
//        currentProduct.getSetOfVariationOptions().addAll(variationOptions);
//      }
//
//      // Handler update image
//      List<String> listOfDeletedImageUrls = request.getListOfDeletedImageUrls();
//      List<Image> tempList = new ArrayList<>();
//      currentProduct.getSetOfImages().forEach(image -> {
//        if (listOfDeletedImageUrls.contains(image.getUrl())) {
//          tempList.add(image);
//        }
//      });
//      currentProduct.getSetOfImages().removeIf(image -> listOfDeletedImageUrls.contains(image.getUrl()));
//      imageRepository.deleteAll(tempList);
//
//      String url;
//      for (String key : mainImage.keySet()) {
//        url = updateProductImage(mainImage.get(key), currentProduct.getImageUrl());
//        if (url != null) {
//          currentProduct.setImageUrl(url);
//        }
//      }
//
//      for (String key : images.keySet()) {
//        url = uploadProductImage(images.get(key), key);
//        if (url != null) {
//          currentProduct.getSetOfImages().add(new Image(null, url, key, currentProduct));
//        }
//      }
//
//      currentProduct = productRepository.saveAndFlush(currentProduct);
//
//      return new DataResponse(ApplicationConstants.PRODUCT_UPDATE_SUCCESSFULLY,
//          productMapper.ProductToProductInfoDto(currentProduct));
//    }
//    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_NOT_FOUND,
//        ApplicationConstants.BAD_REQUEST_CODE);
//  }

  @Override
  public DataResponse deleteProduct(long id) {
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (optionalProduct.isPresent()) {
      Product product = optionalProduct.get();
      product.setDeletedAt(LocalDateTime.now());
      productRepository.saveAndFlush(product);
      return new DataResponse(ApplicationConstants.PRODUCT_DELETE_SUCCESSFULLY, true);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse changeProductStatus(ChangeProductStatusRequest request) {
    long productId = request.getProductId();
    long statusId = request.getStatusId();
    String message = "";
    Optional<Product> optionalProduct = productRepository.findById(productId);
    if (optionalProduct.isPresent()) {
      Optional<ProductStatus> optionalProductStatus = productStatusRepository.findById(statusId);
      if (optionalProductStatus.isPresent()) {
        Product product = optionalProduct.get();
        product.setStatus(optionalProductStatus.get());
        product = productRepository.saveAndFlush(product);
        return new DataResponse(ApplicationConstants.PRODUCT_UPDATE_SUCCESSFULLY, product);
      } else {
        message = ApplicationConstants.PRODUCT_STATUS_NOT_FOUND;
      }
    } else {
      message = ApplicationConstants.PRODUCT_NOT_FOUND;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, message, ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse recommendProducts() {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    List<String> productIds = new ArrayList<>();
    if (userId > 0) {
      List<UserClick> clicks = userClickRepository.findFirst15ByUser_IdIsOrderByCreatedAtDesc(userId);
      if (clicks.size() > 0) {
        LocalDateTime maxDate = clicks.get(clicks.size() - 1).getCreatedAt();
        LocalDateTime minDate = maxDate.minusDays(clickRangeInDay);
        List<UserClick> olderClicks = userClickRepository.findByUser_IdIsAndCreatedAtBetween(userId, minDate, maxDate);
        clicks.addAll(olderClicks);
      }
      productIds.addAll(clicks.stream().map(c -> c.getProduct().getId().toString()).collect(Collectors.toList()));
    }
    List<String> recommendedProductIds = getRecommendedProducts(productIds);
    List<Product> products = productRepository.findByIdInAndStatus_IdInAndDeletedAtNull(
        recommendedProductIds.stream().map(Long::valueOf).collect(
            Collectors.toList()), Arrays.asList(ProductStatusEnum.ONSALE.getId()));
    return new DataResponse(
        products.stream().map(productMapper::ProductToSimpleProductDto).collect(Collectors.toList()));
  }

  private String uploadProductImage(MultipartFile image) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    try {
      String originalFilename = image.getOriginalFilename();
      String fileName = generateFileName(originalFilename, 1);
      Map params = ObjectUtils.asMap(
          "use_filename", true,
          "unique_filename", true,
          "overwrite", false,
          "resource_type", "raw",
          "public_id", imagePath + "/" + fileName
      );
      Map result = cloudinary.uploader().upload(image.getBytes(), params);
      return result.get("secure_url").toString();
    } catch (Exception exception) {
      System.out.println("exception: " + exception);
      return null;
    }
  }

  private String uploadProductImage(String image, String name) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    try {
//      String originalFilename = image.getOriginalFilename();
      String fileName = generateFileName(name, 1);
      Map params = ObjectUtils.asMap(
          "use_filename", true,
          "unique_filename", true,
          "overwrite", false,
          "resource_type", "raw",
          "public_id", imagePath + "/" + fileName
      );
      Map result = cloudinary.uploader().upload(image.getBytes(), params);
      return result.get("secure_url").toString();
    } catch (Exception exception) {
      System.out.println("exception: " + exception);
      return null;
    }
  }

  private String updateProductImage(MultipartFile image, String imageUrl) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    String publicId = findImagePublicId(imageUrl);
    try {
      String originalFilename = image.getOriginalFilename();
      if (originalFilename == null) {
        return null;
      }
      String fileName = generateFileName(originalFilename, 1);
      Map params = ObjectUtils.asMap(
          "use_filename", true,
          "unique_filename", true,
          "overwrite", true,
          "resource_type", "raw",
          "public_id", publicId
      );
      Map result = cloudinary.uploader().upload(image.getBytes(), params);
      return result.get("secure_url").toString();
    } catch (IOException exception) {
      return null;
    }
  }

  private String updateProductImage(String image, String imageUrl) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    String publicId = findImagePublicId(imageUrl);
    try {
//      String originalFilename = image.getOriginalFilename();
//      if (originalFilename == null) {
//        return null;
//      }
//      String fileName = generateFileName(originalFilename, 1);
      Map params = ObjectUtils.asMap(
          "use_filename", true,
          "unique_filename", true,
          "overwrite", true,
          "resource_type", "raw",
          "public_id", publicId
      );
      Map result = cloudinary.uploader().upload(image.getBytes(), params);
      return result.get("secure_url").toString();
    } catch (IOException exception) {
      return null;
    }
  }

  private void deleteProductImage(String imageUrl) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    String publicId = findImagePublicId(imageUrl);
    try {
      cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    } catch (IOException exception) {

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

  private String generateFileName(String orginalFileName, int offset) {
    LocalDateTime now = LocalDateTime.now();
    String dateTimePattern = "yyyyMMddHHmmss";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
    String[] arrOfOriginalFileName = orginalFileName.split("\\.");
    String fileName = arrOfOriginalFileName[0];
    String fileSuffix = arrOfOriginalFileName[1];
    return fileName + "_" + now.format(dateTimeFormatter) + "_" + String.valueOf(offset) + "." + fileSuffix;
  }

  private String findImagePublicId(String url) {
    return url.substring(url.indexOf(imagePath));
  }

  private List<Specification<Product>> createSpecificationsBaseOnLoggedInUser() {
    String role = AuthenticationHelper.getCurrentLoggedInUserRole();
    List<Specification<Product>> specifications = new ArrayList<>();
    specifications.add(ProductSpecification.isNotDeleted());
//    if (role != null && (role.equals(AccountRoleEnum.ROLE_ADMIN.getName()) || role.equals(AccountRoleEnum.ROLE_EMPLOYEE.getName()))) {
//      return specifications;
//    }
    specifications.add(ProductSpecification.isNotHide());
    return specifications;
  }

  public static String generateRandom(int length) {
    Random random = new Random();
    char[] digits = new char[length];
    digits[0] = (char) (random.nextInt(9) + '1');
    for (int i = 1; i < length; i++) {
      digits[i] = (char) (random.nextInt(10) + '0');
    }
    return String.valueOf(digits);
  }

  public List<String> getRecommendedProducts(List<String> productIds) {
    try {
      Gson gsonObj = new Gson();
      WebClient client = WebClient.builder().baseUrl("https://airecomendation-2bd712ac5d3b.herokuapp.com/")
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.post();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/recommend").build());

      Map<String, Object> data = new HashMap<>();
      data.put("ids", productIds);
      String json = gsonObj.toJson(data);

      bodySpec.body(BodyInserters.fromValue(json));

      Mono<RecommendProductResponse> response = bodySpec.retrieve().bodyToMono(RecommendProductResponse.class);
      RecommendProductResponse productResponse = response.block();
      if (productResponse.getStatus() != 200 || productResponse.getIds() == null
          || productResponse.getIds().size() <= 0) {
        logger.error(productResponse.getMessage());
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      return productResponse.getIds();
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }
}
