package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.AuthService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public DataResponse register(@RequestBody @Valid RegisterRequest request) {
    return authService.register(request);
  }
}
