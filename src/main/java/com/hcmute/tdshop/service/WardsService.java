package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface WardsService {
  DataResponse getAllWards(Pageable pageable);
  DataResponse getWardsByDistrictId(long id, Pageable pageable);
}
