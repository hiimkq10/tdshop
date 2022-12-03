package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.mastercategory.AddMasterCategoryRequest;
import com.hcmute.tdshop.dto.mastercategory.UpdateMasterCategoryRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.MasterCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/master-category")
public class MasterCategoryController {
  @Autowired
  MasterCategoryService masterCategoryService;

  @GetMapping("/get-all")
  public DataResponse getAll(Pageable pageable) {
    return masterCategoryService.getAll(pageable);
  }

  @GetMapping("/get/{id}")
  public DataResponse getById(long id) {
    return masterCategoryService.getById(id);
  }

  @PostMapping("/add")
  public DataResponse insertMasterCategory(AddMasterCategoryRequest request) {
    return masterCategoryService.insertMasterCategory(request);
  }

  @PutMapping("/update/{id}")
  public DataResponse updateMasterCategory(@PathVariable(name = "id") long id, UpdateMasterCategoryRequest request) {
    return masterCategoryService.updateMasterCategory(id, request);
  }

  @PostMapping("/delete/{id}")
  public DataResponse deleteMasterCategory(@PathVariable(name = "id") long id) {
    return masterCategoryService.deleteMasterCategory(id);
  }
}
