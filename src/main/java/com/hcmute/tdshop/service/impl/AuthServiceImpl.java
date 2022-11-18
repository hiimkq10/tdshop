package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.mapper.AuthMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.AuthService;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthMapper authMapper;

  @Autowired
  private Helper helper;

  @Override
  public DataResponse register(RegisterRequest request) {
    User user = authMapper.RegisterRequestToUser(request);
    if (user != null) {
      if (helper.checkIfUsernameExisted(user.getUsername())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USERNAME_EXISTED,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      if (helper.checkIfEmailExisted(user.getEmail())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.EMAIL_EXISTED,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      if (helper.checkIfPhoneExisted(user.getPhone())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PHONE_EXISTED,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      user.setIsActive(true);
      user.setIsVerified(false);
      user.setCreatedAt(LocalDateTime.now());
      user = userRepository.save(user);
      return new DataResponse(user);
    }
    return DataResponse.BAD_REQUEST;
  }
}
