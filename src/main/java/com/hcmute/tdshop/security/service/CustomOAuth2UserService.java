package com.hcmute.tdshop.security.service;

import com.hcmute.tdshop.entity.AccountRole;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.enums.AccountRoleEnum;
import com.hcmute.tdshop.enums.AuthProvider;
import com.hcmute.tdshop.exception.OAuth2AuthenticationProcessingException;
import com.hcmute.tdshop.repository.AccountRoleRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.security.model.CustomUserDetails;
import com.hcmute.tdshop.security.oauth2.OAuth2UserInfo;
import com.hcmute.tdshop.security.oauth2.OAuth2UserInfoFactory;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRoleRepository accountRoleRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
    if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }

    Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
    User user;
    if(userOptional.isPresent()) {
      user = userOptional.get();
      if(Objects.isNull(user.getProvider()) || (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())))) {
        throw new OAuth2AuthenticationProcessingException("Account Existed");
      }
    } else {
      user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
    }

    return CustomUserDetails.create(user, oAuth2User.getAttributes());
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user = new User();

    user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
    user.setProviderId(oAuth2UserInfo.getId());
    String[] names = oAuth2UserInfo.getName().split(" ", 2);
    try {
      user.setFirstName(names[0]);
      user.setLastName(names[1]);
    }
    catch (Exception ex) {

    }
    user.setEmail(oAuth2UserInfo.getEmail());
    user.setUsername(oAuth2UserInfo.getEmail());
    user.setPassword(UUID.randomUUID().toString());
    AccountRole role = accountRoleRepository.findById(AccountRoleEnum.ROLE_USER.getId()).get();
    user.setRole(role);
    user.setIsActive(true);
    user.setIsVerified(true);
    user.setCreatedAt(LocalDateTime.now());
    return userRepository.save(user);
  }
}
