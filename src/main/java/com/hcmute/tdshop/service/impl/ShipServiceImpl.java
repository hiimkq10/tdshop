package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ShipRepository;
import com.hcmute.tdshop.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipServiceImpl implements ShipService {

  @Autowired
  ShipRepository shipRepository;

  @Override
  public DataResponse getAll() {
    return new DataResponse(shipRepository.findAll());
  }
}
