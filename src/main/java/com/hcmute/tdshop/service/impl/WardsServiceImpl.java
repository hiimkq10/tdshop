package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.mapper.AddressMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.service.WardsService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WardsServiceImpl implements WardsService {

  @Autowired
  private WardsRepository wardsRepository;

  @Autowired
  private AddressMapper addressMapper;

  public DataResponse getAllWards(Pageable pageable) {
    return new DataResponse(
        wardsRepository.findAll(pageable.getSort()).stream().map(item -> addressMapper.WardsToWardsResponse(item))
            .collect(
                Collectors.toList()));
  }

  @Override
  public DataResponse getWardsByDistrictId(long id, Pageable pageable) {
    return new DataResponse(
        wardsRepository.findByDistrict_Id(id, pageable.getSort()).stream()
            .map(item -> addressMapper.WardsToWardsResponse(item)).collect(
                Collectors.toList()));
  }
}
