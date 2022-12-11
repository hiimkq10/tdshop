package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping("/get")
  public DataResponse getByMasterCategoryId(@RequestParam(name = "master-category", required = false) long id) {
    return categoryService.getByMasterCategoryId(id);
  }
}
