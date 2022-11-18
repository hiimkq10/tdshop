package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface AuthService {
  DataResponse register(RegisterRequest request);
}
