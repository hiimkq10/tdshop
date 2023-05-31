package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.address.AddressToLatAndLngDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

  @Autowired
  LocationService locationService;

  @PostMapping("/address-to-coordinates")
  public DataResponse addressToCoordinates(
      @RequestBody AddressToLatAndLngDto dto,
      Pageable pageable
  ) {
    return locationService.addressToLatAndLng(dto);
  }

  @PostMapping("/check-coordinates-valid")
  public DataResponse checkCoordinateValid(
      @RequestBody CheckCoordinateValidDto dto,
      Pageable pageable
  ) {
    return locationService.checkCoordinateValid(dto);
  }
}
