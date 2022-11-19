package com.hcmute.tdshop.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.entity.AccountRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponse {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("FirstName")
  private String firstName;

  @JsonProperty("LastName")
  private String lastName;

  @JsonProperty("Email")
  private String email;

  @JsonProperty("Phone")
  private String phone;

  @JsonProperty("Birthdate")
  private LocalDate birthdate;

  @JsonProperty("Gender")
  private Boolean gender;

  @JsonProperty("Role")
  private AccountRole role;

  @JsonProperty("IsActive")
  private Boolean isActive;

  @JsonProperty("IsVerified")
  private Boolean isVerified;

  @JsonProperty("CreatedAt")
  private LocalDateTime createdAt;
}
