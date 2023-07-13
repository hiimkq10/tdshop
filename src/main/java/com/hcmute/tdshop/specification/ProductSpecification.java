package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductPromotion;
import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

  public static Specification<Product> hasId(long id) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
  }

  public static Specification<Product> hasSku(String sku) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")),
        "%" + sku.toLowerCase() + "%"));
  }

  public static Specification<Product> hasName(String name) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
        "%" + name.toLowerCase() + "%"));
  }

  public static Specification<Product> hasBrand(String brand) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("brand").get("name")),
        "%" + brand.toLowerCase() + "%"));
  }

  public static Specification<Product> hasBrandId(Long id) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), id)));
  }

  public static Specification<Product> hasPriceLessThanOrEqualTo(double maxPrice) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
  }

  public static Specification<Product> hasPriceGreaterThanOrEqualTo(double minPrice) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
  }

  public static Specification<Product> isNotDeleted() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
  }

  public static Specification<Product> isNotHide() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status").get("id"),
        ProductStatusEnum.HIDE.getId());
  }

  public static Specification<Product> hasVariations(Set<Long> setOfVariationIds) {
    return ((root, query, criteriaBuilder) -> {
//      Subquery<Long> countVariationSubquery = query.subquery(Long.class);
//      Root<Product> countVariations = countVariationSubquery.from(Product.class);
//      Join<VariationOption, Product> variationOptionProductJoin = countVariations.join("setOfVariationOptions", JoinType.RIGHT);
//      countVariationSubquery
//          .select(criteriaBuilder.countDistinct(variationOptionProductJoin.get("variation").get("id")))
//          .where(variationOptionProductJoin.get("id").in(setOfVariationIds));

      Subquery<Long> countVariationSubquery = query.subquery(Long.class);
      Root<VariationOption> countVariations = countVariationSubquery.from(VariationOption.class);
      Join<VariationOption, Product> variationOptionProductJoin = countVariations.join("setOfProducts", JoinType.LEFT);
      countVariationSubquery
          .select(criteriaBuilder.countDistinct(variationOptionProductJoin.getParentPath().get("variation").get("id")))
          .where(variationOptionProductJoin.getParentPath().get("id").in(setOfVariationIds));

      Subquery<Long> selectedProductIDsSubquery = query.subquery(Long.class);
      Root<Product> selectedProductIDs = selectedProductIDsSubquery.from(Product.class);
      Join<VariationOption, Product> productVariationOptionJoin = selectedProductIDs.join("setOfVariationOptions");
      selectedProductIDsSubquery
          .select(selectedProductIDs.get("id"))
          .where(productVariationOptionJoin.get("id").in(setOfVariationIds))
          .groupBy(selectedProductIDs.get("id"))
          .having(criteriaBuilder.equal(criteriaBuilder.count(selectedProductIDs), countVariationSubquery));
      return root.get("id").in(selectedProductIDsSubquery);
    });
  }

  public static Specification<Product> hasCategory(Long categoryId) {
    return ((root, query, criteriaBuilder) -> {
      Subquery<Long> selectedProductIDsSubquery = query.subquery(Long.class);
      Root<Product> selectedProductIDs = selectedProductIDsSubquery.from(Product.class);
      Join<Category, Product> productCategoryJoin = selectedProductIDs.join("setOfCategories");
      selectedProductIDsSubquery
          .select(selectedProductIDs.get("id"))
          .where(criteriaBuilder.equal(productCategoryJoin.get("id"), categoryId));
      return root.get("id").in(selectedProductIDsSubquery);
    });
  }

  public static Specification<Product> hasCategory(Set<Long> categoryIds) {
    return ((root, query, criteriaBuilder) -> {
      Subquery<Long> selectedProductIDsSubquery = query.subquery(Long.class);
      Root<Product> selectedProductIDs = selectedProductIDsSubquery.from(Product.class);
      Join<Category, Product> productCategoryJoin = selectedProductIDs.join("setOfCategories");
      selectedProductIDsSubquery
          .select(selectedProductIDs.get("id"))
          .where(productCategoryJoin.get("id").in(categoryIds));
      return root.get("id").in(selectedProductIDsSubquery);
    });
  }

  public static Specification<Product> sortByPromotion(Direction direction) {
    return (((root, query, criteriaBuilder) -> {
      Subquery<Long> selectedProductIdsSubquery = query.subquery(Long.class);
      Root<Product> selectedProductIds = selectedProductIdsSubquery.from(Product.class);
      Join<Product, ProductPromotion> productProductPromotionJoin = selectedProductIds.join("setOfProductPromotions");
      LocalDateTime now = LocalDateTime.now();
      selectedProductIdsSubquery
          .select(selectedProductIds.get("id"))
          .where(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(selectedProductIds.get("startDate"), now),
              criteriaBuilder.greaterThanOrEqualTo(selectedProductIds.get("endDate"), now)));

      selectedProductIdsSubquery.distinct(true);
      Order promotionOrder = direction == Direction.ASC
          ? criteriaBuilder.asc(selectedProductIds.get("discountRate"))
          : criteriaBuilder.desc(selectedProductIds.get("discountRate"));

      query.orderBy(promotionOrder);
      return criteriaBuilder.equal(root.get("id"), selectedProductIdsSubquery);
    }));
  }

  public static Specification<Product> hasMasterCategory(long masterCategoryId) {
    return ((root, query, criteriaBuilder) -> {
      Subquery<Long> selectedProductIDsSubquery = query.subquery(Long.class);
      Root<Product> selectedProductIDs = selectedProductIDsSubquery.from(Product.class);
      Join<Category, Product> productCategoryJoin = selectedProductIDs.join("setOfCategories");
      selectedProductIDsSubquery
          .select(selectedProductIDs.get("id"))
          .where(productCategoryJoin.get("masterCategory").get("id").in(masterCategoryId));
      return root.get("id").in(selectedProductIDsSubquery);
    });
  }
}
