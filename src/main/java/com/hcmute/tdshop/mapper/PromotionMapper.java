package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.promotion.AddPromotionRequest;
import com.hcmute.tdshop.dto.promotion.TempPromotion;
import com.hcmute.tdshop.dto.promotion.UpdatePromotionRequest;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Promotion;
import com.hcmute.tdshop.repository.CategoryRepository;
import com.hcmute.tdshop.utils.Helper;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PromotionMapper {

  @Autowired
  private CategoryRepository categoryRepository;

  public abstract Promotion AddPromotionRequestToPromotion(AddPromotionRequest request);
  public abstract Promotion UpdatePromotionRequestToPromotion(UpdatePromotionRequest request);

  LocalDateTime StringToLocalDateTime(String str) {
    return Helper.MyLocalDateTimeParser(str);
  }

  Set<Category> SetOfLongToSetOfCategory(Set<Long> categoryIds) {
    if (categoryIds == null || categoryIds.size() == 0) {
      return new HashSet<Category>();
    }
    Set<Category> setOfCategories = new HashSet<>();
    Category category;
    for (Long id : categoryIds) {
      category = categoryRepository.findById(id).orElse(null);
      if (category != null) {
        setOfCategories.add(category);
      }
    }
    return setOfCategories;
  }
}
