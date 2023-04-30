package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.auth.ForgotPasswordRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.EmailService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

  @Autowired
  private EmailService emailService;

  @PostMapping("/send-forgot-password-email")
  public DataResponse sendEmail(@RequestBody @Valid ForgotPasswordRequest request) {
    return emailService.sendForgotPasswordEmail(request);
  }

  @PostMapping("/send-activate-account-email/{id}")
  public DataResponse sendEmail(HttpServletRequest request, @PathVariable(name = "id") long id) {
    return emailService.sendActivateAccountEmail(request, id);
  }
}
