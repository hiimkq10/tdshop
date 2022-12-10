package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.product.AddProductRequest;
import com.hcmute.tdshop.dto.product.ChangeProductStatusRequest;
import com.hcmute.tdshop.dto.product.UpdateProductRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ProductService;
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

  @GetMapping("/get-all")
  public DataResponse getAllProducts(Pageable pageable) {
    return productService.getAllProducts(pageable);
  }

  @GetMapping("/search")
  public DataResponse searchProductsByFilter(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "category-id", required = false, defaultValue = "0") long categoryId,
      @RequestParam(name = "max-price", required = false, defaultValue = "0") double maxPrice,
      @RequestParam(name = "min-price", required = false, defaultValue = "0") double minPrice,
      @RequestParam(name = "variations", required = false) Set<Long> ids,
      Pageable pageable) {
    return productService.searchProductsByFilter(keyword, categoryId, maxPrice, minPrice, ids, pageable);
  }

//  @GetMapping("/search")
//  public DataResponse searchProductsByKeyword(
//      @RequestParam(name = "keyword", required = false) String keyword,
//      Pageable pageable) {
//    return productService.searchProductsByKeyword(keyword, pageable);
//  }

  @GetMapping("/get/{id}")
  public DataResponse getProductById(@PathVariable(name = "id") Long id) {
    return productService.getProductById(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertProduct(
      @RequestPart(value = "ProductInfo") @Valid AddProductRequest request,
      @RequestPart(value = "MainImage") MultipartFile mainImage,
      @RequestPart(value = "OtherImage") List<MultipartFile> images) {
    return productService.insertProduct(request, mainImage, images);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updateProduct(
      @PathVariable(name = "id") long id,
      @RequestPart(value = "ProductInfo") @Valid UpdateProductRequest request,
      @RequestPart(value = "MainImage") MultipartFile mainImage,
      @RequestPart(value = "OtherImage") List<MultipartFile> images) {
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
}
