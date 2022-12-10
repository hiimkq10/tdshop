package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.user.UpdateUserInfoRequest;
import com.hcmute.tdshop.dto.user.UserResponse;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.mapper.UserMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.UserService;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private Helper helper;

  @Override
  public DataResponse getUserInfo() {
    long id = AuthenticationHelper.getCurrentLoggedInUserId();
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      UserResponse userResponse = userMapper.UserToUserResponse(user);
      return new DataResponse(userResponse);
    }
    return new DataResponse(ApplicationConstants.NOT_FOUND, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.NOT_FOUND_CODE);
  }

  @Override
  public DataResponse updateUserInfo(UpdateUserInfoRequest request) {
    long id = AuthenticationHelper.getCurrentLoggedInUserId();
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      User currentUser = optionalUser.get();
      User userToUpdate = userMapper.UpdateUserInfoRequestToUser(request);
      if (!helper.checkIfStringIsBlank(userToUpdate.getFirstName())) {
        currentUser.setFirstName(userToUpdate.getFirstName());
      }
      if (!helper.checkIfStringIsBlank(userToUpdate.getLastName())) {
        currentUser.setLastName(userToUpdate.getLastName());
      }
      if (!helper.checkIfStringIsBlank(userToUpdate.getEmail())) {
        currentUser.setEmail(userToUpdate.getEmail());
      }
      if (helper.checkIfStringIsBlank(userToUpdate.getPhone())) {
        currentUser.setPhone(userToUpdate.getPhone());
      }
      if (userToUpdate.getBirthdate() != null) {
        currentUser.setBirthdate(userToUpdate.getBirthdate());
      }
      if (userToUpdate.getIsActive() != null) {
        currentUser.setIsActive(userToUpdate.getIsActive());
      }
      return new DataResponse(userMapper.UserToUserResponse(userRepository.saveAndFlush(currentUser)));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse banUser(long id) {
    long adminId = AuthenticationHelper.getCurrentLoggedInUserId();
    if (adminId == id) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_BAN_THEMSELF, ApplicationConstants.BAD_REQUEST_CODE);
    }
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setIsActive(false);
      userRepository.saveAndFlush(user);
      return new DataResponse(ApplicationConstants.SUCCESSFUL, ApplicationConstants.USER_BAN_SUCCESSFULLY, ApplicationConstants.SUCCESSFUL_CODE);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }
}
