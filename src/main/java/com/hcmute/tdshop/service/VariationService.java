package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.variation.AddVariationRequest;
import com.hcmute.tdshop.dto.variation.UpdateVariationRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface VariationService {
  DataResponse getAll(Pageable pageable);
  DataResponse getByMasterCategory(long id);
  DataResponse insertVariation(AddVariationRequest request);
  DataResponse updateVariation(long id, UpdateVariationRequest request);
  DataResponse deleteVariation(long id);
}
