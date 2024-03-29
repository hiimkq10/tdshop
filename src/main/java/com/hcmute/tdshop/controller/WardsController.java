package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.WardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
  public DataResponse getAllWards(Pageable pageable) {
    return wardsService.getAllWards(pageable);
  }

  @GetMapping("/get-by-district/{district-id}")
  public DataResponse getWardsByDistrictId(@PathVariable(name = "district-id") long id, Pageable pageable) {
    return wardsService.getWardsByDistrictId(id, pageable);
  }
}
