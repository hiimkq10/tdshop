package com.hcmute.tdshop.security.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.tdshop.dto.security.LoginRequest;
import com.hcmute.tdshop.dto.security.LoginResponse;
import com.hcmute.tdshop.dto.security.UserInfo;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.security.jwt.JwtTokenProvider;
import com.hcmute.tdshop.security.model.CustomUserDetails;
import com.hcmute.tdshop.security.service.CustomUserDetailsService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService customUserDetailsService;

  private final JwtTokenProvider jwtTokenProvider;

  public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.customUserDetailsService = customUserDetailsService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    ObjectMapper objectMapper = new ObjectMapper();
    LoginRequest loginRequest;
    String errorMessage = "";
    int status = ApplicationConstants.UNAUTHORIZED_CODE;
    Map<String, Long> errorData = new HashMap<>();
    response.setContentType(APPLICATION_JSON_VALUE);
    String username = "";
    String password = "";
    try {
      loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
      username = loginRequest.getUsername();
      password = loginRequest.getPassword();
      if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
      } else {
        errorMessage = ApplicationConstants.USERNAME_OR_PASSWORD_MISSING;
      }
    } catch (DisabledException ex) {
      status = 10001;
      errorMessage = ApplicationConstants.ACCOUNT_INACTIVE;
      CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
      errorData.put("id", userDetails.getUser().getId());
    } catch (BadCredentialsException ex) {
      errorMessage = ApplicationConstants.USERNAME_OR_PASSWORD_INCORRECT;
    } catch (LockedException ex) {
      errorMessage = ApplicationConstants.ACCOUNT_BANNED;
    } catch (IOException ex) {
      errorMessage = ApplicationConstants.UNEXPECTED_ERROR;
      log.error("Error logging in: {}", ex.getMessage());
    }


    try {
      objectMapper.writeValue(
          response.getOutputStream(),
          new DataResponse(ApplicationConstants.UNAUTHORIZED, errorMessage, errorData, status)
      );
    } catch (IOException ex) {
      log.error("Error logging in: {}", ex.getMessage());
    }
    return null;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    try {
      CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();
      String accessToken = jwtTokenProvider.generateAccessToken(user, request);
      String refreshToken = jwtTokenProvider.generateRefreshToken(user, request);
      UserInfo userInfo = CustomUserDetailsToUserInfo(user);
      LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken, userInfo);
      new ObjectMapper().writeValue(response.getOutputStream(), new DataResponse(loginResponse));
    } catch (IOException ex) {
      log.error("Error logging in: {}", ex.getMessage());
    }
  }

  private UserInfo CustomUserDetailsToUserInfo(CustomUserDetails userDetails) {
    if (userDetails == null) {
      return null;
    }

    UserInfo userInfo = new UserInfo();
    userInfo.setId(userDetails.getUser().getId());
    userInfo.setFirstName(userDetails.getUser().getFirstName());
    userInfo.setLastName(userDetails.getUser().getLastName());
    userInfo.setEmail(userDetails.getUser().getEmail());
    userInfo.setUsername(userDetails.getUser().getUsername());
    userInfo.setRole(userDetails.getUser().getRole().getName());

    return userInfo;
  }
}
