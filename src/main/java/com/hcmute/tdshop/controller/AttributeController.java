package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attribute")
public class AttributeController {

  @Autowired
  AttributeService attributeService;

  @GetMapping()
  public DataResponse getAttributesByAttributeSet(@RequestParam(name = "set-id") long id) {
    return attributeService.getAttributesByAttributeSet(id);
  }
}
