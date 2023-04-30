package com.hcmute.tdshop.security.oauth2;

import static com.hcmute.tdshop.security.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.hcmute.tdshop.exception.OAuth2AuthenticationProcessingException;
import com.hcmute.tdshop.security.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.hcmute.tdshop.utils.CookieUtils;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  public OAuth2AuthenticationFailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
    this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue)
        .orElse(("/"));
    int errorStatus = ApplicationConstants.BAD_REQUEST_CODE;
    if (exception instanceof OAuth2AuthenticationProcessingException) {
      errorStatus = 10002;
    }
    targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("error", errorStatus)
        .build().toUriString();

    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
