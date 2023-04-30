package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.auth.ForgotPasswordRequest;
import com.hcmute.tdshop.model.DataResponse;
import javax.servlet.http.HttpServletRequest;

public interface EmailService {
  DataResponse sendForgotPasswordEmail(ForgotPasswordRequest request);
  DataResponse sendActivateAccountEmail(HttpServletRequest request, Long id);
}
