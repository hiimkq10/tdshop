package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.entity.Province;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProvinceRepository;
import com.hcmute.tdshop.service.ProvinceService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImpl implements ProvinceService {
  @Autowired
  private ProvinceRepository provinceRepository;

  @Override
  public DataResponse getAllProvince() {
    return new DataResponse(provinceRepository.findAll());
  }

  @Override
  public DataResponse getProvinceById(long id) {
    Optional<Province> optionalProvince = provinceRepository.findById(id);
    return optionalProvince.map(DataResponse::new).orElseGet(
        () -> new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PROVINCE_NOT_FOUND,
            ApplicationConstants.BAD_REQUEST_CODE));
  }
}
