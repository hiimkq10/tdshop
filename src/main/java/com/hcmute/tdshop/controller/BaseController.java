package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

  @GetMapping
  public DataResponse base() {
    return DataResponse.SUCCESSFUL;
  }
}
