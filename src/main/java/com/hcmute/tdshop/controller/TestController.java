package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
  @GetMapping("/")
  public DataResponse test() {
    AppProperties appProperties = new AppProperties();
    return new DataResponse(appProperties.getOauth2().getAuthorizedRedirectUris());
  }
}
