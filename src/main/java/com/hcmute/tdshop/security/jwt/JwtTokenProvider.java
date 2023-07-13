package com.hcmute.tdshop.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.security.model.CustomUserDetails;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(AppProperties.class)
public class JwtTokenProvider {
  @Autowired
  AppProperties appProperties;

  private Algorithm jwtAlgorithm;

  @PostConstruct
  public void postConstruct() {
    jwtAlgorithm = Algorithm.HMAC256(appProperties.getJwtSecret().getBytes());
  }

  public String generateAccessToken(CustomUserDetails user, HttpServletRequest request) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + appProperties.getJwtAccessTokenExpiration());
    return JWT.create()
        .withSubject(user.getUsername())
        .withClaim(ApplicationConstants.JWT_CLAIM_ID, user.getUser().getId().toString())
        .withClaim(ApplicationConstants.JWT_CLAIM_FIRST_NAME, user.getUser().getFirstName())
        .withClaim(ApplicationConstants.JWT_CLAIM_LAST_NAME, user.getUser().getLastName())
        .withClaim(ApplicationConstants.JWT_CLAIM_ROLE, user.getUser().getRole().getName())
        .withExpiresAt(expiryDate)
        .withIssuer(request.getRequestURL().toString())
        .withClaim("roles",
            user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .sign(this.getJWTAlgorithm());
  }

  public String generateRefreshToken(CustomUserDetails user, HttpServletRequest request) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + appProperties.getJwtRefreshTokenExpiration());
    return JWT.create()
        .withSubject(user.getUsername())
        .withClaim(ApplicationConstants.JWT_CLAIM_ID, user.getUser().getId().toString())
        .withClaim(ApplicationConstants.JWT_CLAIM_FIRST_NAME, user.getUser().getFirstName())
        .withClaim(ApplicationConstants.JWT_CLAIM_LAST_NAME, user.getUser().getLastName())
        .withExpiresAt(expiryDate)
        .withIssuer(request.getRequestURL().toString())
        .sign(this.getJWTAlgorithm());
  }

  public Algorithm getJWTAlgorithm() {
    return this.jwtAlgorithm;
  }
}
