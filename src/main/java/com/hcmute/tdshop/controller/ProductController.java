package com.hcmute.tdshop.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.hcmute.tdshop.dto.product.AddClickRequest;
import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.dto.product.ChangeProductStatusRequest;
import com.hcmute.tdshop.dto.product.UpdateProductRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ClickService;
import com.hcmute.tdshop.service.ProductService;
import com.hcmute.tdshop.service.SubscriptionService;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @Autowired
  private SubscriptionService subscriptionService;

  @Autowired
  private ClickService clickService;

  @GetMapping("/get-all")
  public DataResponse getAllProducts(Pageable pageable) {
    return productService.getAllProducts(pageable);
  }

  @GetMapping("/get/{id}")
  public DataResponse getProductById(@PathVariable(name = "id") Long id) {
    return productService.getProductById(id);
  }

  @GetMapping("/search")
  public DataResponse searchProductsByFilter(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "category-id", required = false, defaultValue = "0") long categoryId,
      @RequestParam(name = "max-price", required = false, defaultValue = "0") double maxPrice,
      @RequestParam(name = "min-price", required = false, defaultValue = "0") double minPrice,
      @RequestParam(name = "brand", required = false) String brand,
      @RequestParam(name = "brand-id", required = false, defaultValue = "0") long brandId,
      @RequestParam(name = "variations", required = false) Set<Long> ids,
      @RequestParam(name = "master-category-id", required = false, defaultValue = "0") long masterCategoryId,
      Pageable pageable) {
    return productService.searchProductsByFilter(keyword, categoryId, maxPrice, minPrice, brand, brandId, ids,
        masterCategoryId,
        pageable);
  }

  @GetMapping("/admin/search")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse searchProductsByFilterForAdmin(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "category-id", required = false, defaultValue = "0") long categoryId,
      @RequestParam(name = "max-price", required = false, defaultValue = "0") double maxPrice,
      @RequestParam(name = "min-price", required = false, defaultValue = "0") double minPrice,
      @RequestParam(name = "brand", required = false) String brand,
      @RequestParam(name = "brand-id", required = false, defaultValue = "0") long brandId,
      @RequestParam(name = "variations", required = false) Set<Long> ids,
      Pageable pageable) {
    return productService.searchProductsByFilterForAdmin(keyword, categoryId, maxPrice, minPrice, brand, brandId, ids,
        pageable);
  }

  @GetMapping("/admin/get")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse getProductsByFilterForAdmin(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "category-id", required = false, defaultValue = "0") long categoryId,
      @RequestParam(name = "max-price", required = false, defaultValue = "0") double maxPrice,
      @RequestParam(name = "min-price", required = false, defaultValue = "0") double minPrice,
      @RequestParam(name = "brand", required = false) String brand,
      @RequestParam(name = "brand-id", required = false, defaultValue = "0") long brandId,
      @RequestParam(name = "variations", required = false) Set<Long> ids,
      Pageable pageable) {
    return productService.getProductsByFilterForAdmin(keyword, categoryId, maxPrice, minPrice, brand, brandId, ids,
        pageable);
  }

  @GetMapping("/admin/get/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse getProductByIdForAdmin(@PathVariable(name = "id") Long id) {
    return productService.getProductByIdForAdmin(id);
  }

  @PostMapping(value = "/add", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertProduct(
      @RequestPart(value = "ProductInfo") @Valid AddProductRequest request,
      @RequestPart(value = "MainImage", required = false) MultipartFile mainImage,
      @RequestPart(value = "OtherImage", required = false) List<MultipartFile> images) {
    return productService.insertProduct(request, mainImage, images);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updateProduct(
      @PathVariable(name = "id") long id,
      @RequestPart(value = "ProductInfo") @Valid UpdateProductRequest request,
      @RequestPart(value = "MainImage", required = false) MultipartFile mainImage,
      @RequestPart(value = "OtherImage", required = false) List<MultipartFile> images) {
    return productService.updateProduct(id, request, mainImage, images);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deleteProduct(@PathVariable(name = "id") long id) {
    return productService.deleteProduct(id);
  }

  @PostMapping("/change-status")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse changeProductStatus(@RequestBody @Valid ChangeProductStatusRequest request) {
    return productService.changeProductStatus(request);
  }

  @PostMapping("/follow/{product-id}")
  public DataResponse follow(@PathVariable(name = "product-id") long id) {
    return subscriptionService.subscribe(id);
  }

  @PostMapping("/un-follow/{product-id}")
  public DataResponse unFollow(@PathVariable(name = "product-id") long id) {
    return subscriptionService.unSubscribe(id);
  }

  @GetMapping("/check-follow/{product-id}")
  public DataResponse checkFollow(@PathVariable(name = "product-id") long id) {
    return subscriptionService.checkFollow(id);
  }

  @PostMapping("/click")
  public DataResponse clickProduct(@RequestBody AddClickRequest request) {
    return clickService.addClick(request);
  }

  @GetMapping("/recommend")
  public DataResponse recommendProducts() {
    return productService.recommendProducts();
  }
}
