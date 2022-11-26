package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;

public interface ProvinceService {
  public DataResponse getAllProvince();
  public DataResponse getProvinceById(long id);
}
