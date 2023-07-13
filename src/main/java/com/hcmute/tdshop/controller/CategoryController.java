package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.category.AddCategoryRequest;
import com.hcmute.tdshop.dto.category.UpdateCategoryRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.CategoryService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping("/get-all")
  public DataResponse getAll(Pageable pageable) {
    return categoryService.getAll(pageable);
  }

  @GetMapping("/get/{id}")
  public DataResponse getById(@PathVariable(name = "id") long id) {
    return categoryService.getById(id);
  }

  @GetMapping("/get")
  public DataResponse getByMasterCategoryId(@RequestParam(name = "master-category", required = false) long id) {
    return categoryService.getByMasterCategoryId(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertCategory(@RequestBody @Valid AddCategoryRequest request) {
    return categoryService.insertCategory(request);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updateCategory(@PathVariable(name = "id") long id,
      @RequestBody @Valid UpdateCategoryRequest request) {
    return categoryService.updateCategory(id, request);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deleteCategory(@PathVariable(name = "id") long id) {
    return categoryService.deleteCategory(id);
  }
}
