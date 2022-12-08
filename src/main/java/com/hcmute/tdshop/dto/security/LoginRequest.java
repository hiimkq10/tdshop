package com.hcmute.tdshop.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
  @JsonProperty("Username")
  private String username;

  @JsonProperty("Password")
  private String password;
}
