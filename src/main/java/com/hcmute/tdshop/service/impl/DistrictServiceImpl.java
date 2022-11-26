package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.DistrictRepository;
import com.hcmute.tdshop.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictServiceImpl implements DistrictService {
  @Autowired
  private DistrictRepository districtRepository;

  @Override
  public DataResponse getAllDistrict() {
    return new DataResponse(districtRepository.findAll());
  }

  @Override
  public DataResponse getDistrictByProvinceId(long id) {
    return new DataResponse(districtRepository.findByProvince_Id(id));
  }
}
