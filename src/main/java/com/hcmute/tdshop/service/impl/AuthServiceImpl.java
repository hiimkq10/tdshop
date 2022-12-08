package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.auth.ChangePasswordRequest;
import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordRequest;
import com.hcmute.tdshop.dto.auth.ResetPasswordVerificationRequest;
import com.hcmute.tdshop.entity.Token;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.enums.AccountRoleEnum;
import com.hcmute.tdshop.mapper.AuthMapper;
import com.hcmute.tdshop.mapper.UserMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AccountRoleRepository;
import com.hcmute.tdshop.repository.TokenRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.AuthService;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRoleRepository accountRoleRepository;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private AuthMapper authMapper;

  @Autowired
  private Helper helper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserMapper userMapper;

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
      user.setPassword(encode(user.getPassword()));
      user.setRole(accountRoleRepository.findById(AccountRoleEnum.ROLE_USER.getId()).get());
      user.setIsActive(true);
      user.setIsVerified(false);
      user.setCreatedAt(LocalDateTime.now());
      user = userRepository.save(user);
      return new DataResponse(userMapper.UserToUserResponse(user));
    }
    return DataResponse.BAD_REQUEST;
  }

  @Override
  public DataResponse changePassword(long id, ChangePasswordRequest request) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      String currentPassword = request.getCurrentPassword();
      String newPassword = request.getNewPassword();
      String confirmPassword = request.getConfirmPassword();
      if (!checkIfCurrentPasswordCorrent(currentPassword, user.getPassword())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CURRENT_PASSWORD_WRONG,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      if (!newPassword.equals(confirmPassword)) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CONFIRM_PASSWORD_NOT_MATCH,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      user.setPassword(encode(newPassword));
      userRepository.saveAndFlush(user);
      return new DataResponse(ApplicationConstants.CHANGE_PASSWORD_SUCCESSFULLY, true);
    }
    return new DataResponse(ApplicationConstants.NOT_FOUND, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.NOT_FOUND_CODE);
  }

  @Override
  @Transactional
  public DataResponse resetPassword(ResetPasswordRequest request) {
    String message = "";
    Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      LocalDateTime now = LocalDateTime.now();
      Token confirmToken = tokenRepository.findByUser_Id(user.getId()).orElse(null);
      if (confirmToken == null) {
        message = ApplicationConstants.UNEXPECTED_ERROR;
      }
      else if (confirmToken.getConfirmedAt() != null) {
        if (now.isBefore(confirmToken.getConfirmedAt().plusMinutes(Helper.RESET_PASSWORD_DURATION))) {
          String newPassword = request.getNewPassword();
          String confirmPassword = request.getConfirmPassword();
          if (newPassword.equals(confirmPassword)) {
            user.setPassword(encode(newPassword));
            tokenRepository.delete(confirmToken);
            userRepository.saveAndFlush(user);
            return new DataResponse(ApplicationConstants.RESET_PASSWORD_SUCCESSFULLY, true);
          }
          else {
            message = ApplicationConstants.CONFIRM_PASSWORD_NOT_MATCH;
          }
        } else {
          message = ApplicationConstants.RESET_PASSWORD_DURATION_OVER;
        }
      } else {
        message = ApplicationConstants.TOKEN_NOT_CONFIRMED;
      }
    }
    else {
      message = ApplicationConstants.USER_NOT_FOUND;
    }
    return new DataResponse(ApplicationConstants.NOT_FOUND, message, ApplicationConstants.NOT_FOUND_CODE);
  }

  @Override
  public DataResponse resetPasswordVerification(ResetPasswordVerificationRequest request) {
    String email = request.getEmail();
    String token = request.getToken();
    String message = "";
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      LocalDateTime now = LocalDateTime.now();

      Token confirmToken = tokenRepository.findByUser_Id(user.getId()).orElse(null);
      if (confirmToken == null) {
        message = ApplicationConstants.TOKEN_WRONG;
      }
      else if (now.isBefore(confirmToken.getExpiredAt())) {
        if (confirmToken.getConfirmedAt() == null) {
          if (token.equals(confirmToken.getCode())) {
            confirmToken.setConfirmedAt(now);
            tokenRepository.saveAndFlush(confirmToken);
            return new DataResponse(ApplicationConstants.SUCCESSFUL, true);
          } else {
            message = ApplicationConstants.TOKEN_WRONG;
          }
        }
        else {
          message = ApplicationConstants.TOKEN_USED;
        }
      } else {
        message = ApplicationConstants.TOKEN_EXPIRED;
      }
    }
    else {
      message = ApplicationConstants.USER_NOT_FOUND;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, message, ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  @Transactional
  public DataResponse activateAccount(Long id, String token) {
    Optional<User> optionalUser = userRepository.findById(id);
    String message = "";
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (!user.getIsVerified()) {
        LocalDateTime now = LocalDateTime.now();

        Token confirmToken = tokenRepository.findByUser_Id(user.getId()).orElse(null);
        if (confirmToken == null) {
          message = ApplicationConstants.TOKEN_WRONG;
        }
        else if (now.isBefore(confirmToken.getExpiredAt())) {
          if (token.equals(confirmToken.getCode())) {
            if (confirmToken.getConfirmedAt() == null) {
              user.setIsVerified(true);
              tokenRepository.delete(confirmToken);
              userRepository.saveAndFlush(user);
              return new DataResponse(ApplicationConstants.ACCOUNT_ACTIVATED, true);
            }
            else {
              message = ApplicationConstants.TOKEN_USED;
            }
          }
          else {
            message = ApplicationConstants.TOKEN_WRONG;
          }
        } else {
          message = ApplicationConstants.TOKEN_EXPIRED;
        }
      }
      else {
        message = ApplicationConstants.ACCOUNT_ACTIVATED;
      }
    }
    else {
      message = ApplicationConstants.USER_NOT_FOUND;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, message, ApplicationConstants.BAD_REQUEST_CODE);
  }

  private boolean checkIfCurrentPasswordCorrent(String currentPassword, String encodedPassword) {
    return passwordEncoder.matches(currentPassword, encodedPassword);
  }
  private String encode(String str) {
    return passwordEncoder.encode(str);
  }
}
