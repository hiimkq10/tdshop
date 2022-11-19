package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.auth.ChangePasswordRequest;
import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordVerificationRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface AuthService {
  DataResponse register(RegisterRequest request);
  DataResponse changePassword(long id, ChangePasswordRequest request);
  DataResponse resetPassword(ResetPasswordRequest request);
  DataResponse resetPasswordVerification(ResetPasswordVerificationRequest request);
  DataResponse activateAccount(Long id, String token);
}
