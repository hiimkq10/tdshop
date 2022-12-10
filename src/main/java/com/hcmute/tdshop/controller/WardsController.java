package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.WardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wards")
public class WardsController {

  @Autowired
  private WardsService wardsService;

  @GetMapping("/get-all")
  public DataResponse getAllWards() {
    return wardsService.getAllWards();
  }

  @GetMapping("/get-by-district/{wards-id}")
  public DataResponse getWardsByDistrictId(@PathVariable(name = "wards-id") long id) {
    return wardsService.getWardsByDistrictId(id);
  }
}
