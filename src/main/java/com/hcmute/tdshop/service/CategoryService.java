package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.category.AddCategoryRequest;
import com.hcmute.tdshop.dto.category.UpdateCategoryRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
  DataResponse getAll(Pageable pageable);
  DataResponse getById(long id);
  DataResponse getByMasterCategoryId(long id);
  DataResponse insertCategory(AddCategoryRequest request);
  DataResponse updateCategory(long id, UpdateCategoryRequest request);
  DataResponse deleteCategory(long id);
}
