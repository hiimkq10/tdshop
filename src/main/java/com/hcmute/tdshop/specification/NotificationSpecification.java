package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.Notification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class NotificationSpecification {

  public static Specification<Notification> isSendAll(Boolean sendAll) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sendAll"), sendAll);
  }

  public static Specification<Notification> hasId(long id) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
  }

  public static Specification<Notification> fromDate(LocalDateTime createdAt) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAt);
  }

}
