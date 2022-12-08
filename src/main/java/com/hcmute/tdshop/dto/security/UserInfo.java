package com.hcmute.tdshop.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfo {
  @JsonProperty("Id")
  private Long id;

  @JsonProperty("FirstName")
  private String firstName;

  @JsonProperty("LastName")
  private String lastName;

  @JsonProperty("Username")
  private String username;

  @JsonProperty("Email")
  private String email;

  @JsonProperty("Role")
  private String role;

  @JsonProperty("IsActive")
  private Boolean isActive = true;
}
