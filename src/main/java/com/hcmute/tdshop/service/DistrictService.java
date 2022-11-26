package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;

public interface DistrictService {
  public DataResponse getAllDistrict();
  public DataResponse getDistrictByProvinceId(long id);
}
