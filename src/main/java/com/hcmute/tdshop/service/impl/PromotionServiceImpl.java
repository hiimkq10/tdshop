package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.promotion.AddPromotionRequest;
import com.hcmute.tdshop.dto.promotion.UpdatePromotionRequest;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductPromotion;
import com.hcmute.tdshop.entity.Promotion;
import com.hcmute.tdshop.mapper.PromotionMapper;
import com.hcmute.tdshop.model.DataResponse;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

  private static final Comparator<LocalDateTime> compareDateTime = new Comparator<LocalDateTime>() {
    @Override
    public int compare(LocalDateTime o1, LocalDateTime o2) {
      return o1.compareTo(o2);
    }
  };

  @Override
  public DataResponse getAll(Pageable pageable) {
    Page<Promotion> pageOfPromotions = promotionRepository.findAll(pageable);
    return new DataResponse(pageOfPromotions);
  }

  @Override
  public DataResponse getPromotion(Long id, String keyword, Double fromRate, Double toRate, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
    List<Specification<Promotion>> listOfSpecifications = new ArrayList<>();
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
    return new DataResponse(pageOfPromotions);
  }

  @Override
  public DataResponse getById(Long id) {
    Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
    return new DataResponse(optionalPromotion.orElse(null));
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
      if (updateRequire) {
        promotionRepository.saveAndFlush(currentPromotion);
        UpdateProductPromotion(currentPromotion);
      }
      return new DataResponse(ApplicationConstants.PROMOTION_UPDATE_SUCCESSFULLY, Boolean.valueOf(true));
    }
    return new DataResponse(ApplicationConstants.PROMOTION_NOT_FOUND, null);
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

  private void UpdateProductPromotion(final Promotion promotion) {
    Thread thread = new Thread(new Runnable() {
      @Override
      @Transactional
      public void run() {
        List<Product> listOfProducts = productRepository.findAll(ProductSpecification
            .hasCategory(promotion.getSetOfCategories().stream().map(promotion -> promotion.getId())
                .collect(Collectors.toSet())));

        for (Product product : listOfProducts) {
          List<Promotion> listOfPromotions = promotionRepository.findAll(
              PromotionSpecification.hasCategory(promotion.getSetOfCategories().stream().map(c -> c.getId())
                  .collect(Collectors.toSet())));

          product.getSetOfProductPromotions().removeAll(listOfPromotions);
          promotionRepository.deleteAll(listOfPromotions);

          List<LocalDateTime> listOfDateTime = new ArrayList<>();
          listOfPromotions.forEach(p -> {
            listOfDateTime.add(p.getStartDate());
            listOfDateTime.add(p.getEndDate());
          });
          listOfDateTime.sort(compareDateTime);
          int size = listOfDateTime.size();
          int size1 = listOfPromotions.size();
          LocalDateTime start;
          LocalDateTime end;
          double max = 0;
          List<Double> listOfDiscountRate = new ArrayList<>();
          Promotion temp;
          for (int i = 0; i < size - 1; i++) {
            start = listOfDateTime.get(i);
            end = listOfDateTime.get(i + 1);
            max = 0;
            listOfDiscountRate.clear();

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
            listOfDiscountRate.remove(max);
            double totalDiscount = max + listOfDiscountRate.size() * 0.05;
            ProductPromotion productPromotion = new ProductPromotion(null, "", "", totalDiscount, start, end, null, product);
            product.getSetOfProductPromotions().add(productPromotion);
          }
          productRepository.saveAndFlush(product);
        }
      }
    });
    thread.start();
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
