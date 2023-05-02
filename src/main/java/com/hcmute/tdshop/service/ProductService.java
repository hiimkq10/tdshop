package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.dto.product.ChangeProductStatusRequest;
import com.hcmute.tdshop.dto.product.UpdateProductRequest;
import com.hcmute.tdshop.model.DataResponse;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

  public DataResponse getAllProducts(Pageable page);

  public DataResponse searchProductsByFilter(String keyword, long categoryId, double maxPrice, double minPrice,
      String brand, Long brandId, Set<Long> variationOptionIds, Pageable page);

  public DataResponse searchProductsByFilterForAdmin(String keyword, long categoryId, double maxPrice, double minPrice,
      String brand, Long brandId, Set<Long> variationOptionIds, Pageable page);

  public DataResponse getProductsByFilterForAdmin(String keyword, long categoryId, double maxPrice, double minPrice,
      String brand, Long brandId, Set<Long> variationOptionIds, Pageable page);

  public DataResponse searchProductsByKeyword(String keyword, Pageable page);

  public DataResponse getProductByIdForAdmin(long id);
  public DataResponse getProductById(long id);

    public DataResponse insertProduct(AddProductRequest request, MultipartFile mainImage, List<MultipartFile> images);
//  public DataResponse insertProduct(AddProductRequest request);

    public DataResponse updateProduct(long id, UpdateProductRequest request, MultipartFile mainImage, List<MultipartFile> images);
//  DataResponse updateProduct(long id, UpdateProductRequest request);

  public DataResponse deleteProduct(long id);

  public DataResponse changeProductStatus(ChangeProductStatusRequest request);
}
