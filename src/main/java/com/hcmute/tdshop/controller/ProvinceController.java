package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/province")
public class ProvinceController {

  @Autowired
  private ProvinceService provinceService;

  @GetMapping("/get-all")
  public DataResponse getAllProvince(Pageable pageable) {
    return provinceService.getAllProvince(pageable);
  }

  @GetMapping("/{id}")
  public DataResponse getProvinceById(@PathVariable(name = "id") long id) {
    return provinceService.getProvinceById(id);
  }
}
