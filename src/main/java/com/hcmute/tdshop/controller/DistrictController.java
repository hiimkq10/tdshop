package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/district")
public class DistrictController {

  @Autowired
  private DistrictService districtService;

  @GetMapping("/")
  public DataResponse getAllDistrict() {
    return districtService.getAllDistrict();
  }

  @GetMapping("/get-by-province/{province-id}")
  public DataResponse getDistrictByProvinceId(@PathVariable(name = "province-id") long id) {
    return districtService.getDistrictByProvinceId(id);
  }
}
