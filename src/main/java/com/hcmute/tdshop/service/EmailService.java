package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.auth.ForgotPasswordRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface EmailService {
  DataResponse sendForgotPasswordEmail(ForgotPasswordRequest request);
  DataResponse sendActivateAccountEmail(Long id);
}
