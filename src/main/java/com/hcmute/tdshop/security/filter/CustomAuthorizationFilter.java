package com.hcmute.tdshop.security.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.tdshop.dto.security.UserInfo;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Data
public class CustomAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    boolean isIndentifed = false; // if jwt is valid, then set isIndentified true
    String errorMessage = "";
    if (request.getServletPath().equals("/login") || request.getServletPath().equals("/token/refresh")) {
      filterChain.doFilter(request, response);
    } else {
      String authorizationHeader = request.getHeader(AUTHORIZATION);
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        try {
          String token = authorizationHeader.substring("Bearer ".length());
          JWTVerifier verifier = JWT.require(Helper.JWT_ALGORITHM).build();
          DecodedJWT decodedJWT = verifier.verify(token);
          String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
          Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
          UserInfo userInfo = getUserInfoFromJWT(decodedJWT);
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          filterChain.doFilter(request, response);
          isIndentifed = true;
        } catch (Exception ex) {
          log.error("Error logging in: {}", ex.getMessage());
          errorMessage = ex.getMessage();
        }
      } else {
        errorMessage = ApplicationConstants.JWT_TOKEN_MISSING;
      }
      if (!isIndentifed) {
        response.setStatus(ApplicationConstants.FORBIDDEN_CODE);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(
            response.getOutputStream(),
            new DataResponse(
                ApplicationConstants.FORBIDDEN,
                errorMessage,
                ApplicationConstants.FORBIDDEN_CODE
            )
        );
      }
    }
  }

  private UserInfo getUserInfoFromJWT(DecodedJWT decodedJWT) {
    if (decodedJWT == null) {
      return null;
    }

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername(decodedJWT.getSubject());
    userInfo.setId(Long.valueOf(decodedJWT.getClaim(ApplicationConstants.JWT_CLAIM_ID).asString()));
    userInfo.setFirstName(decodedJWT.getClaim(ApplicationConstants.JWT_CLAIM_FIRST_NAME).asString());
    userInfo.setLastName(decodedJWT.getClaim(ApplicationConstants.JWT_CLAIM_LAST_NAME).asString());

    return userInfo;
  }
}
