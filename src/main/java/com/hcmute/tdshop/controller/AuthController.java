package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.auth.ChangePasswordRequest;
import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordVerificationRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/change-password/{id}")
  public DataResponse changePassword(@PathVariable(name = "id") long id,
      @RequestBody @Valid ChangePasswordRequest request) {
    return authService.changePassword(id, request);
  }

  @PostMapping("/register")
  public DataResponse register(@RequestBody @Valid RegisterRequest request) {
    return authService.register(request);
  }

  @PostMapping("/reset-password")
  public DataResponse resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    return authService.resetPassword(request);
  }

  @PostMapping("/reset-password-verification")
  public DataResponse resetPasswordVerification(
      @Valid @RequestBody ResetPasswordVerificationRequest request) {
    return authService.resetPasswordVerification(request);
  }

  @GetMapping("/activate/{id}")
  @ResponseBody
  public DataResponse activateAccount(
      @PathVariable(name = "id") long id,
      @RequestParam(name = "token") String token,
      HttpServletRequest request,
      HttpServletResponse response) {
    return authService.activateAccount(id, token, request, response);
  }
}
