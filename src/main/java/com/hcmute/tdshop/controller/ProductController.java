package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ProductService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping("/get-all")
  public DataResponse getAllProducts(Pageable pageable) {
    return productService.getAllProducts(pageable);
  }

  @GetMapping("/filter")
  public DataResponse searchProductsByFilter(
      @RequestParam(name = "category-id", required = false, defaultValue = "0") long categoryId,
      @RequestParam(name = "max-price", required = false, defaultValue = "0") double maxPrice,
      @RequestParam(name = "min-price", required = false, defaultValue = "0") double minPrice,
      @RequestParam(name = "variations", required = false) Set<Long> ids,
      Pageable pageable) {
    return productService.searchProductsByFilter(categoryId, maxPrice, minPrice, ids, pageable);
  }

  @GetMapping("/search")
  public DataResponse searchProductsByKeyword(
      @RequestParam(name = "keyword", required = false) String keyword,
      Pageable pageable) {
    return productService.searchProductsByKeyword(keyword, pageable);
  }

  @GetMapping("/get/{id}")
  public DataResponse getProductById(@PathVariable(name = "id") Long id) {
    return productService.getProductById(id);
  }

}
