package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.ShopOrder;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
  public static Specification<ShopOrder> hasId(Long id) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id)));
  }

  public static Specification<ShopOrder> hasStatus(Long id) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderStatus").get("id"), id)));
  }

  public static Specification<ShopOrder> hasUser(Long id) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), id)));
  }

  public static Specification<ShopOrder> isNotDeleted() {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"))));
  }
}
