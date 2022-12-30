package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.promotion.AddPromotionRequest;
import com.hcmute.tdshop.dto.promotion.PromotionResponse;
import com.hcmute.tdshop.dto.promotion.UpdatePromotionRequest;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductPromotion;
import com.hcmute.tdshop.entity.Promotion;
import com.hcmute.tdshop.mapper.PromotionMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProductPromotionRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.PromotionRepository;
import com.hcmute.tdshop.service.PromotionService;
import com.hcmute.tdshop.specification.ProductSpecification;
import com.hcmute.tdshop.specification.PromotionSpecification;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl implements PromotionService {

  @Autowired
  PromotionRepository promotionRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  PromotionMapper promotionMapper;

  @Autowired
  ProductPromotionRepository productPromotionRepository;

  private static final Comparator<LocalDateTime> compareDateTime = new Comparator<LocalDateTime>() {
    @Override
    public int compare(LocalDateTime o1, LocalDateTime o2) {
      return o1.compareTo(o2);
    }
  };

  @Override
  public DataResponse getAll(Pageable pageable) {
    Specification<Promotion> conditions = Specification.where(PromotionSpecification.isNotDeleted());
    Page<Promotion> pageOfPromotions = promotionRepository.findAll(conditions, pageable);
    Page<PromotionResponse> pageOfPromotionResponse = new PageImpl<>(
        pageOfPromotions.getContent().stream().map(promotionMapper::PromotionToPromotionResponse).collect(Collectors.toList()),
        pageable,
        pageOfPromotions.getTotalElements()
    );
    return new DataResponse(pageOfPromotionResponse);
  }

  @Override
  public DataResponse getPromotion(Long id, String keyword, Double fromRate, Double toRate, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
    List<Specification<Promotion>> listOfSpecifications = new ArrayList<>();
    listOfSpecifications.add(PromotionSpecification.isNotDeleted());
    if (id > 0 ) {
      listOfSpecifications.add(PromotionSpecification.hasId(id));
    }
    if (keyword != null) {
      List<Specification<Promotion>> keywordSpecifications = new ArrayList<>();
      keywordSpecifications.add(PromotionSpecification.hasName(keyword));
      keywordSpecifications.add(PromotionSpecification.hasDescription(keyword));
      listOfSpecifications.add(SpecificationHelper.or(keywordSpecifications));
    }
    if (fromRate > 0) {
      listOfSpecifications.add(PromotionSpecification.fromRate(fromRate));
    }
    if (toRate > 0) {
      listOfSpecifications.add(PromotionSpecification.toRate(toRate));
    }
    if (startDate != null) {
      listOfSpecifications.add(PromotionSpecification.fromDate(startDate));
    }
    if (endDate != null) {
      listOfSpecifications.add(PromotionSpecification.toDate(endDate));
    }
    Specification<Promotion> conditions = SpecificationHelper.and(listOfSpecifications);
    Page<Promotion> pageOfPromotions = promotionRepository.findAll(conditions, pageable);
    Page<PromotionResponse> pageOfPromotionResponse = new PageImpl<>(
        pageOfPromotions.getContent().stream().map(promotionMapper::PromotionToPromotionResponse).collect(Collectors.toList()),
        pageable,
        pageOfPromotions.getTotalElements()
    );
    return new DataResponse(pageOfPromotionResponse);
  }

  @Override
  public DataResponse getById(Long id) {
    Optional<Promotion> optionalPromotion = promotionRepository.findByIdAndDeletedAtIsNull(id);
    if (optionalPromotion.isPresent()) {
      return new DataResponse(promotionMapper.PromotionToPromotionResponse(optionalPromotion.get()));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PROMOTION_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  @Transactional
  public DataResponse insertPromotion(AddPromotionRequest request) {
    Promotion promotion = promotionMapper.AddPromotionRequestToPromotion(request);
    if (!isStartDateBeforEndDate(promotion.getStartDate(), promotion.getEndDate())) {
      return new DataResponse(ApplicationConstants.PROMOTION_START_DATE_NOT_BEFORE_END_DATE, null);
    }
    Iterator<Category> iterator = promotion.getSetOfCategories().iterator();
    Category category;
    List<Category> childrenCategories = new ArrayList<>();
    while (iterator.hasNext()) {
      category = iterator.next();
      if (category.getChildren() != null && category.getChildren().size() > 0) {
        childrenCategories.addAll(category.getChildren());
        iterator.remove();
      }
    }
    promotion.getSetOfCategories().addAll(childrenCategories);
    promotion = promotionRepository.save(promotion);
    UpdateProductPromotion(promotion);
    return new DataResponse(ApplicationConstants.PROMOTION_ADD_SUCCESSFULLY, Boolean.valueOf(true));
  }

  @Override
  @Transactional
  public DataResponse updatePromotion(Long id, UpdatePromotionRequest request) {
    Promotion promotionToUpdate = promotionMapper.UpdatePromotionRequestToPromotion(request);
    Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
    boolean updateRequire = false;
    if (optionalPromotion.isPresent()) {
      Promotion currentPromotion = optionalPromotion.get();
      if (!Helper.checkIfStringIsBlank(promotionToUpdate.getName())) {
        currentPromotion.setName(promotionToUpdate.getName());
      }
      currentPromotion.setDescription(promotionToUpdate.getDescription());
      if (promotionToUpdate.getDiscountRate() > 0 && promotionToUpdate.getDiscountRate() != currentPromotion.getDiscountRate()) {
        currentPromotion.setDiscountRate(promotionToUpdate.getDiscountRate());
        updateRequire = true;
      }
      if (promotionToUpdate.getStartDate() != null && !(promotionToUpdate.getStartDate().isEqual(currentPromotion.getStartDate()))) {
        if (isStartDateBeforEndDate(promotionToUpdate.getStartDate(), currentPromotion.getEndDate())) {
          currentPromotion.setStartDate(promotionToUpdate.getStartDate());
          updateRequire = true;
        }
      }
      if (promotionToUpdate.getEndDate() != null && !(promotionToUpdate.getEndDate().isEqual(currentPromotion.getEndDate()))) {
        if (isStartDateBeforEndDate(currentPromotion.getStartDate(), promotionToUpdate.getEndDate())) {
          currentPromotion.setStartDate(promotionToUpdate.getStartDate());
          updateRequire = true;
        }
      }
      currentPromotion = promotionRepository.saveAndFlush(currentPromotion);
      if (updateRequire) {
        UpdateProductPromotion(currentPromotion);
      }
      return new DataResponse(ApplicationConstants.PROMOTION_UPDATE_SUCCESSFULLY, Boolean.valueOf(true));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PROMOTION_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  @Transactional
  public DataResponse deletePromotion(Long id) {
    Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
    if (optionalPromotion.isPresent()) {
      Promotion promotion = optionalPromotion.get();
      promotionRepository.delete(promotion);
      UpdateProductPromotion(promotion);
      return new DataResponse(ApplicationConstants.PROMOTION_DELETE_SUCCESSFULLY, Boolean.valueOf(true));
    }
    return new DataResponse(ApplicationConstants.PROMOTION_NOT_FOUND, null);
  }

  private void UpdateProductPromotion(Promotion promotion) {
    List<Product> listOfProducts = productRepository.findAll(ProductSpecification
        .hasCategory(promotion.getSetOfCategories().stream().map(p -> p.getId())
            .collect(Collectors.toSet())));

    for (Product product : listOfProducts) {
      List<Promotion> listOfPromotions = promotionRepository.findAll(
          PromotionSpecification.hasCategory(product.getSetOfCategories().stream().map(c -> c.getId())
              .collect(Collectors.toSet())));

      List<ProductPromotion> listOfProductPromotions = productPromotionRepository.findByProductId(product.getId());
      if (product.getSetOfProductAttributes() != null && product.getSetOfProductAttributes().size() > 0) {
        productPromotionRepository.deleteAll(listOfProductPromotions);
        product.getSetOfProductPromotions().clear();
      }
      product.setSetOfProductPromotions(new HashSet<>());

      List<LocalDateTime> listOfDateTime = new ArrayList<>();
      for (Promotion p : listOfPromotions) {
        listOfDateTime.add(p.getStartDate());
        listOfDateTime.add(p.getEndDate());
      }
      listOfDateTime = new ArrayList<>(new HashSet<>(listOfDateTime));
      listOfDateTime.sort(compareDateTime);
      int size = listOfDateTime.size();
      int size1 = listOfPromotions.size();
      LocalDateTime start;
      LocalDateTime end;
      double max = 0;
      List<Double> listOfDiscountRate = new ArrayList<>();
      Promotion temp;
      List<ProductPromotion> productPromotions = new ArrayList<>();
      for (int i = 0; i < size - 1; i++) {
        start = listOfDateTime.get(i);
        end = listOfDateTime.get(i + 1);
        max = 0;
        listOfDiscountRate.clear();
        System.out.println("test: " + start);
        System.out.println("test: " + end);
        for (int j = 0; j < size1; j++) {
          temp = listOfPromotions.get(j);
          if (isBeforeOrEqual(temp.getStartDate(), start) && isAfterOrEqual(temp.getEndDate(), end)) {
            if (temp.getDiscountRate() > max) {
              max = temp.getDiscountRate();
            }
            if (temp.getDiscountRate() > 0.05) {
              listOfDiscountRate.add(temp.getDiscountRate());
            }
          }
        }
        if (listOfDiscountRate.size() > 0 && max != 0) {
          listOfDiscountRate.remove(max);
          double totalDiscount = max + listOfDiscountRate.size() * 0.05;
          ProductPromotion productPromotion = new ProductPromotion(null, "", "", Math.floor(totalDiscount * 100) / 100, start, end, null, product);
          product.getSetOfProductPromotions().add(productPromotion);
          productPromotions.add(productPromotion);
        }
      }
      productPromotionRepository.saveAll(productPromotions);
    }
  }

  private boolean isBeforeOrEqual(LocalDateTime date1, LocalDateTime date2){
    return date1.isBefore(date2) || date1.isEqual(date2);
  }

  private boolean isAfterOrEqual(LocalDateTime date1, LocalDateTime date2){
    return date1.isAfter(date2) || date1.isEqual(date2);
  }

  private boolean isStartDateBeforEndDate(LocalDateTime start, LocalDateTime end) {
    return start.isBefore(end);
  }
}
