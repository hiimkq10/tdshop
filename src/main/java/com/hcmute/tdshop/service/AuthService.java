package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.auth.ChangePasswordRequest;
import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordVerificationRequest;
import com.hcmute.tdshop.model.DataResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
  DataResponse register(HttpServletRequest request, RegisterRequest input);
  DataResponse changePassword(long id, ChangePasswordRequest request);
  DataResponse resetPassword(ResetPasswordRequest request);
  DataResponse resetPasswordVerification(ResetPasswordVerificationRequest request);
  DataResponse activateAccount(Long id, String token, HttpServletRequest request, HttpServletResponse response);
  DataResponse regenerateAccessToken(HttpServletRequest request);
}
