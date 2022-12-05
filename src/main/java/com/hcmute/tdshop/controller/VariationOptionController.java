package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.VariationOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/variation")
public class VariationOptionController {

  @Autowired
  VariationOptionService variationOptionService;

  @GetMapping
  public DataResponse getVariationOptionByVariation(@RequestParam("variation-id") long id) {
    return variationOptionService.getVariationOptionByVariation(id);
  }
}
