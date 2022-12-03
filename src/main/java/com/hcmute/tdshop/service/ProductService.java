package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.model.DataResponse;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
  public DataResponse getAllProducts(Pageable page);
  public DataResponse searchProductsByFilter(long categoryId, double maxPrice, double minPrice, Set<Long> variationOptionIds, Pageable page);
  public DataResponse searchProductsByKeyword(String keyword, Pageable page);
  public DataResponse getProductById(long id);
  public DataResponse insertProduct(AddProductRequest request, List<MultipartFile> images);
}
