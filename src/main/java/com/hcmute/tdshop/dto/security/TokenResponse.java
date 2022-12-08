package com.hcmute.tdshop.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
  @JsonProperty("AccessToken")
  private String accessToken;

  @JsonProperty("RefreshToken")
  private String refreshToken;
}
