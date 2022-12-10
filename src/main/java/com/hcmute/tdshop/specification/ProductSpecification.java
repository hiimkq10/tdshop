package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
  public static Specification<Product> hasId(long id) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
  }

  public static Specification<Product> hasSku(String sku) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), "%" + sku.toLowerCase() + "%"));
  }

  public static Specification<Product> hasName(String name) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
  }

  public static Specification<Product> hasBrand(String brand) {
    return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("brand").get("name")), "%" + brand.toLowerCase() + "%"));
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
    return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status").get("id"), ProductStatusEnum.HIDE.getId());
  }
}
