package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.Review;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

  public static Specification<Review> hasUser(long userId) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId)));
  }

  public static Specification<Review> hasProduct(long productId) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("id"), productId)));
  }

  public static Specification<Review> fromDateTime(LocalDateTime dateTime) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), dateTime)));
  }

  public static Specification<Review> isVerified(boolean isVerified) {
    return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isVerified"), isVerified)));
  }
}
