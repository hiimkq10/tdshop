package com.hcmute.tdshop.utils;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationHelper {

  public static <T> Specification<T> and(List<Specification<T>> specifications) {
    int size = specifications.size();
    if (size <= 0) {
      return null;
    }
    Specification<T> specification = Specification.where(specifications.get(0));
    for (int i = 1; i < size; i++) {
      specification = specification.and(specifications.get(i));
    }
    return specification;
  }

  public static <T> Specification<T> or(List<Specification<T>> specifications) {
    int size = specifications.size();
    if (size <= 0) {
      return null;
    }
    Specification<T> specification = Specification.where(specifications.get(0));
    for (int i = 1; i < size; i++) {
      specification = specification.or(specifications.get(i));
    }
    return specification;
  }
}
