package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.attributeset.AddAttributeSetRequest;
import com.hcmute.tdshop.dto.attributeset.UpdateAttributeSetRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface AttributeSetService {
  DataResponse getAll(Pageable pageable);
  DataResponse insertAttributeSet(AddAttributeSetRequest request);
  DataResponse updateAttributeSet(long id, UpdateAttributeSetRequest request);
  DataResponse deleteAttributeSet(long id);
}
