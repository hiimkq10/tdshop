package com.hcmute.tdshop.specification;

import com.hcmute.tdshop.entity.User;
import java.util.List;
import javax.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
  public static Specification<User> hasId(long id) {
    return ((root, query, criteriaBuilder) -> {
      return criteriaBuilder.equal(root.get("id"), id);
    });
  }

  public static Specification<User> exceptId(long id) {
    return ((root, query, criteriaBuilder) -> {
      return criteriaBuilder.notEqual(root.get("id"), id);
    });
  }

  public static Specification<User> hasName(String name) {
    return (((root, query, criteriaBuilder) -> {
      Expression<String> fullName = criteriaBuilder.concat(root.get("firstName"), " ");
      fullName = criteriaBuilder.concat(fullName, root.get("lastName"));
      Expression<String> fullNameReverse = criteriaBuilder.concat(root.get("lastName"), " ");
      fullNameReverse = criteriaBuilder.concat(fullNameReverse, root.get("firstName"));
      return criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(fullName), "%" + name.toLowerCase() + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(fullNameReverse), "%" + name.toLowerCase() + "%"));
    }));
  }

  public static Specification<User> hasEmail(String email) {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }));
  }

  public static Specification<User> hasPhone(String phone) {
    return ((root, query, criteriaBuilder) -> {
      return criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
    });
  }

  public static Specification<User> hasRole(long roleId) {
    return ((root, query, criteriaBuilder) -> {
      return criteriaBuilder.equal(root.get("role").get("id"), roleId);
    });
  }

  public static Specification<User> isNotDeleted() {
    return (((root, query, criteriaBuilder) -> {
      return criteriaBuilder.isNull(root.get("deletedAt"));
    }));
  }

  public static Specification<User> hasRoles(List<Long> roleIds) {
    return ((root, query, criteriaBuilder) -> {
      return root.get("role").get("id").in(roleIds);
    });
  }
}
