package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.mapper.AddressMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.DistrictRepository;
import com.hcmute.tdshop.service.DistrictService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DistrictServiceImpl implements DistrictService {

  @Autowired
  private DistrictRepository districtRepository;

  @Autowired
  private AddressMapper addressMapper;

  @Override
  public DataResponse getAllDistrict(Pageable pageable) {
    return new DataResponse(
        districtRepository.findAll(pageable.getSort()).stream().map(item -> addressMapper.DistrictToDistrictResponse(item)).collect(
            Collectors.toList()));
  }

  @Override
  public DataResponse getDistrictByProvinceId(long id, Pageable pageable) {
    return new DataResponse(
        districtRepository.findByProvince_Id(id, pageable.getSort()).stream().map(item -> addressMapper.DistrictToDistrictResponse(item))
            .collect(
                Collectors.toList()));
  }
}
