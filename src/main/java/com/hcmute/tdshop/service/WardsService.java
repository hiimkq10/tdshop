package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;

public interface WardsService {
  DataResponse getAllWards();
  DataResponse getWardsByDistrictId(long id);
}
