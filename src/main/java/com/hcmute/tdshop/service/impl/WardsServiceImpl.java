package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.service.WardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WardsServiceImpl implements WardsService {

  @Autowired
  private WardsRepository wardsRepository;

  public DataResponse getAllWards() {
    return new DataResponse(wardsRepository.findAll());
  }
  @Override
  public DataResponse getWardsByDistrictId(long id) {
    return new DataResponse(wardsRepository.findByDistrict_Id(id));
  }
}
