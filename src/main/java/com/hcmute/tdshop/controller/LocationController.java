package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.address.AddressToLatAndLngDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

  @Autowired
  LocationService locationService;

//  @Autowired
//  ExcelUtil excelUtil;

//  @GetMapping("/import-areas")
//  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
//  public DataResponse importAreas()
//      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//    excelUtil.insertDataToDatabase();
//    return DataResponse.SUCCESSFUL;
//  }

  @PostMapping("/address-to-coordinates")
  public DataResponse addressToCoordinates(
      @RequestBody AddressToLatAndLngDto dto
  ) {
    return locationService.addressToLatAndLng(dto);
  }

  @PostMapping("/check-coordinates-valid")
  public DataResponse checkCoordinateValid(
      @RequestBody CheckCoordinateValidDto dto
  ) {
    return locationService.checkCoordinateValid(dto);
  }
}
