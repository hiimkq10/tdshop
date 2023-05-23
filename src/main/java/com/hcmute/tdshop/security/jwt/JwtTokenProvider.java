package com.hcmute.tdshop.security.jwt;

import com.auth0.jwt.JWT;
import com.hcmute.tdshop.security.model.CustomUserDetails;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;

public class JwtTokenProvider {

  public static String generateAccessToken(CustomUserDetails user, HttpServletRequest request) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + Helper.JWT_ACCESS_TOKEN_EXPIRATION);
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
        .sign(Helper.JWT_ALGORITHM);
  }

  public static String generateRefreshToken(CustomUserDetails user, HttpServletRequest request) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + Helper.JWT_REFRESH_TOKEN_EXPIRATION);
    return JWT.create()
        .withSubject(user.getUsername())
        .withClaim(ApplicationConstants.JWT_CLAIM_ID, user.getUser().getId().toString())
        .withClaim(ApplicationConstants.JWT_CLAIM_FIRST_NAME, user.getUser().getFirstName())
        .withClaim(ApplicationConstants.JWT_CLAIM_LAST_NAME, user.getUser().getLastName())
        .withExpiresAt(expiryDate)
        .withIssuer(request.getRequestURL().toString())
        .sign(Helper.JWT_ALGORITHM);
  }
}
