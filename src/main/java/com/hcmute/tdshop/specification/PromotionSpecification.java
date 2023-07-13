package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Promotion;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class PromotionSpecification {

  public static Specification<Promotion> hasName(String name) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }));
  }

  public static Specification<Promotion> hasDescription(String description) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
          "%" + description.toLowerCase() + "%");
    }));
  }

  public static Specification<Promotion> fromRate(Double rate) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.greaterThanOrEqualTo(root.get("discountRate"), rate);
    }));
  }

  public static Specification<Promotion> toRate(Double rate) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.lessThanOrEqualTo(root.get("discountRate"), rate);
    }));
  }

  public static Specification<Promotion> hasId(Long id) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.equal(root.get("id"), id);
    }));
  }

  public static Specification<Promotion> fromDate(LocalDateTime date) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), date);
    }));
  }

  public static Specification<Promotion> toDate(LocalDateTime date) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), date);
    }));
  }

  public static Specification<Promotion> hasCategory(Set<Long> categoryIds) {
    return (((root, query, criteriaBuilder) -> {
      Subquery<Long> selectedPromotionIdsSubquery = query.subquery(Long.class);
      Root<Promotion> selectedPromotionIds = selectedPromotionIdsSubquery.from(Promotion.class);
      Join<Category, Promotion> categoryPromotionJoin = selectedPromotionIds.join("setOfCategories");
      selectedPromotionIdsSubquery
          .select(selectedPromotionIds.get("id"))
          .where(categoryPromotionJoin.get("id").in(categoryIds));
      return root.get("id").in(selectedPromotionIdsSubquery);
    }));
  }

  public static Specification<Promotion> isNotDeleted() {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.isNull(root.get("deletedAt"));
    }));
  }
}
