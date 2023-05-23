package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.user.UpdateUserInfoRequest;
import com.hcmute.tdshop.dto.user.UserResponse;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.enums.AccountRoleEnum;
import com.hcmute.tdshop.mapper.UserMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.scheduledtask.ProductScheduledTask;
import com.hcmute.tdshop.service.UserService;
import com.hcmute.tdshop.specification.UserSpecification;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private static final Logger logger = LoggerFactory.getLogger(ProductScheduledTask.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private Helper helper;

  @Override
  public DataResponse getUsers(Long id, String keyword, Long roleId, Pageable pageable) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    String role = AuthenticationHelper.getCurrentLoggedInUserRole();
    if (userId == 0 || Strings.isBlank(role)) {
      logger.error(String.format(ApplicationConstants.USER_ID_OR_ROLE_NOT_FOUND, userId));
    }
    List<Long> allowedRoles = new ArrayList<>();
    allowedRoles.add(AccountRoleEnum.ROLE_USER.getId());
    if (role.equals(AccountRoleEnum.ROLE_ADMIN.getName())) {
      allowedRoles.add(AccountRoleEnum.ROLE_EMPLOYEE.getId());
      allowedRoles.add(AccountRoleEnum.ROLE_ADMIN.getId());
    }
    List<Specification<User>> specifications = new ArrayList<>();
    specifications.add(UserSpecification.isNotDeleted());
    specifications.add(UserSpecification.hasRoles(allowedRoles));
    specifications.add(UserSpecification.exceptId(userId));
    if (id > 0) {
      specifications.add(UserSpecification.hasId(id));
    }
    if (!Helper.checkIfStringIsBlank(keyword)) {
      List<Specification<User>> orSpecifications = new ArrayList<>();
      orSpecifications.add(UserSpecification.hasName(keyword));
      orSpecifications.add(UserSpecification.hasEmail(keyword));
      orSpecifications.add(UserSpecification.hasPhone(keyword));
      specifications.add(SpecificationHelper.or(orSpecifications));
    }
    if (roleId > 0) {
      specifications.add(UserSpecification.hasRole(roleId));
    }
    Specification<User> conditions = SpecificationHelper.and(specifications);
    Page<User> pageOfUsers = userRepository.findAll(conditions, pageable);
    Page<UserResponse> pageOfUsersResponse = new PageImpl<UserResponse>(
        pageOfUsers.getContent().stream().map(userMapper::UserToUserResponse).collect(Collectors.toList()),
        pageable,
        pageOfUsers.getTotalElements()
    );
    return new DataResponse(pageOfUsers);
  }

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
      System.out.println(userToUpdate.getPhone());
      if (!helper.checkIfStringIsBlank(userToUpdate.getFirstName())) {
        currentUser.setFirstName(userToUpdate.getFirstName());
      }
      if (!helper.checkIfStringIsBlank(userToUpdate.getLastName())) {
        currentUser.setLastName(userToUpdate.getLastName());
      }
      if (!helper.checkIfStringIsBlank(userToUpdate.getEmail())) {
        currentUser.setEmail(userToUpdate.getEmail());
      }
      if (!helper.checkIfStringIsBlank(userToUpdate.getPhone())) {
        currentUser.setPhone(userToUpdate.getPhone());
      }
      if (userToUpdate.getBirthdate() != null) {
        currentUser.setBirthdate(userToUpdate.getBirthdate());
      }
      if (userToUpdate.getIsActive() != null) {
        currentUser.setIsActive(userToUpdate.getIsActive());
      }
      if (userToUpdate.getGender() != null) {
        currentUser.setGender(userToUpdate.getGender());
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
    Optional<User> optionalUser = userRepository.findByIdAndDeletedAtIsNull(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setIsActive(false);
      userRepository.saveAndFlush(user);
      return new DataResponse(ApplicationConstants.SUCCESSFUL, ApplicationConstants.USER_BAN_SUCCESSFULLY, ApplicationConstants.SUCCESSFUL_CODE);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse unBanUser(long id) {
    long adminId = AuthenticationHelper.getCurrentLoggedInUserId();
    if (adminId == id) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_UN_BAN_THEMSELF, ApplicationConstants.BAD_REQUEST_CODE);
    }
    Optional<User> optionalUser = userRepository.findByIdAndDeletedAtIsNull(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setIsActive(true);
      userRepository.saveAndFlush(user);
      return new DataResponse(ApplicationConstants.SUCCESSFUL, ApplicationConstants.USER_UN_BAN_SUCCESSFULLY, ApplicationConstants.SUCCESSFUL_CODE);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }
}
