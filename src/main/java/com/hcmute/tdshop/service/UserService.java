package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.user.UpdateUserInfoRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
  DataResponse getUsers(Long id, String keyword, Long roleId, Pageable pageable);
  DataResponse getUserInfo();
  DataResponse updateUserInfo(UpdateUserInfoRequest request);
  DataResponse banUser(long id);
}
