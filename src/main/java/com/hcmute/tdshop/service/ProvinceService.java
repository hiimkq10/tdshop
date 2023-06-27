package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface ProvinceService {
  public DataResponse getAllProvince(Pageable pageable);
  public DataResponse getProvinceById(long id);
}
