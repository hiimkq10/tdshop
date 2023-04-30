package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ReviewService;
import com.twilio.rest.microvisor.v1.App;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {
  @Autowired
  AppProperties appProperties;
  @Autowired
  private ReviewService reviewService;
  @GetMapping("/")
  public DataResponse test() {
    AppProperties appProperties = new AppProperties();
    return new DataResponse(appProperties.getAuthorizedRedirectUris());
  }

  @PostMapping("/posttest")
  public void postTest() {
    appProperties.getAuthorizedRedirectUris().forEach(item -> System.out.println(item));
//    authorizedRedirectUris.forEach(item -> System.out.println(item));
    System.out.println("Test");
//    reviewService.getAll(Pageable.ofSize(10));
//    return new DataResponse(appProperties.getOauth2().getAuthorizedRedirectUris());
  }
}
