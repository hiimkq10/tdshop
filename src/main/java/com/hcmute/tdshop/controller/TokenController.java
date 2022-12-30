package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {
  @Autowired
  AuthService authService;

  @PostMapping("/refresh")
  public DataResponse regenerateAccessToken(HttpServletRequest request) {
    return authService.regenerateAccessToken(request);
  }
}
