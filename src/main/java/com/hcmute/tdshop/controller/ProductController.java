package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
  @Autowired
  private ProductService productService;

  @GetMapping("/user/get-all")
  public DataResponse getAllProducts(@RequestParam(name = "page") int page) {
    return productService.getAllProducts(page);
  }
}
