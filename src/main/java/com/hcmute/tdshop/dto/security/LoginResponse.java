package com.hcmute.tdshop.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
  @JsonProperty("AccessToken")
  private String accessToken;

  @JsonProperty("RefreshToken")
  private String refreshToken;

  @JsonProperty("UserInfo")
  private UserInfo userInfo;
}
