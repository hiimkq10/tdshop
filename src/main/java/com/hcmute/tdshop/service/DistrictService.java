package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface DistrictService {
  public DataResponse getAllDistrict(Pageable pageable);
  public DataResponse getDistrictByProvinceId(long id, Pageable pageable);
}
