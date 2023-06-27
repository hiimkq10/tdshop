package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.address.AddressToLatAndLngDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.LocationService;
import com.hcmute.tdshop.utils.ExcelUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @Autowired
  ExcelUtil excelUtil;

  @GetMapping("/import-areas")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse testExcel()
      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    excelUtil.insertDataToDatabase();
    return DataResponse.SUCCESSFUL;
  }

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
