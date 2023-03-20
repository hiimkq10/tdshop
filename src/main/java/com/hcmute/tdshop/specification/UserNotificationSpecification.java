package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.UserNotification;
import org.springframework.data.jpa.domain.Specification;

public class UserNotificationSpecification {

  public static Specification<UserNotification> isNotDeleted() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), false);
  }

  public static Specification<UserNotification> hasUserId(Long userId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
  }
}
