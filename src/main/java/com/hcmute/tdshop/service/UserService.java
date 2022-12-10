package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.user.UpdateUserInfoRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface UserService {

  DataResponse getUserInfo();
  DataResponse updateUserInfo(UpdateUserInfoRequest request);
  DataResponse banUser(long id);
}
