package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.entity.Province;
import com.hcmute.tdshop.mapper.AddressMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProvinceRepository;
import com.hcmute.tdshop.service.ProvinceService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImpl implements ProvinceService {

  @Autowired
  private ProvinceRepository provinceRepository;

  @Autowired
  private AddressMapper addressMapper;

  @Override
  public DataResponse getAllProvince(Pageable pageable) {
    return new DataResponse(
        provinceRepository.findAll(pageable.getSort()).stream().map(item -> addressMapper.ProvinceToProvinceResponse(item)).collect(
            Collectors.toList()));
  }

  @Override
  public DataResponse getProvinceById(long id) {
    Optional<Province> optionalProvince = provinceRepository.findById(id);
    if (!optionalProvince.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PROVINCE_NOT_FOUND,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    return new DataResponse(addressMapper.ProvinceToProvinceResponse(optionalProvince.get()));
  }
}
