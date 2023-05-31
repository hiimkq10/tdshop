package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.address.AddressToLatAndLngDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidDto;
import com.hcmute.tdshop.model.DataResponse;

public interface LocationService {
  DataResponse addressToLatAndLng(AddressToLatAndLngDto dto);
  DataResponse checkCoordinateValid(CheckCoordinateValidDto dto);
}
