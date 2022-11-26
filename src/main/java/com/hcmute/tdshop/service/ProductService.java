package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ProductService {
  public DataResponse getAllProducts(Pageable page);
  public DataResponse searchProductsByFilter(long categoryId, double maxPrice, double minPrice, Set<Long> variationOptionIds, Pageable page);
  public DataResponse searchProductsByKeyword(String keyword, Pageable page);
  public DataResponse getProductById(long id);
}
