package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT(19)")
  private Long id;

  @Column(name = "first_name", columnDefinition = "NVARCHAR(50)", nullable = false)
  @JsonProperty("FirstName")
  private String firstName;

  @Column(name = "last_name", columnDefinition = "NVARCHAR(50)", nullable = false)
  @JsonProperty("LastName")
  private String lastName;

  @Column(name = "email", columnDefinition = "NVARCHAR(100)", nullable = false, unique = true)
  @Email
  @JsonProperty("Email")
  private String email;

  @Column(name = "phone", columnDefinition = "NVARCHAR(10)", unique = true)
  @JsonProperty("Phone")
  private String phone;

  @Column(name = "birthdate")
  private LocalDate birthdate;

  @Column(name = "gender")
  private Boolean gender;

  @Column(name = "username", columnDefinition = "NVARCHAR(50)", nullable = false, unique = true)
  @JsonProperty("Username")
  private String username;

  @Column(name = "password", nullable = false)
  @JsonProperty("Password")
  private String password;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id", nullable = false)
  private AccountRole role;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "is_verified", nullable = false)
  private Boolean isVerified = false;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Address> setOfAddress;
}
