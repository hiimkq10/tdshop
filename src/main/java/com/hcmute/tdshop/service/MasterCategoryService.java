package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.mastercategory.AddMasterCategoryRequest;
import com.hcmute.tdshop.dto.mastercategory.UpdateMasterCategoryRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface MasterCategoryService {
  DataResponse getAll(Pageable pageable);
  DataResponse getById(long id);
  DataResponse insertMasterCategory(AddMasterCategoryRequest request);
  DataResponse updateMasterCategory(long id, UpdateMasterCategoryRequest request);
  DataResponse deleteMasterCategory(long id);
}
