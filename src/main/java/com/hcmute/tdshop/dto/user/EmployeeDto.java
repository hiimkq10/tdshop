package com.hcmute.tdshop.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("FirstName")
  private String firstName;

  @JsonProperty("LastName")
  private String lastName;

  @JsonProperty("Birthdate")
  private LocalDate birthdate;

  @JsonProperty("Gender")
  private Boolean gender;

  @JsonProperty("IsActive")
  private Boolean isActive;

  @JsonProperty("IsVerified")
  private Boolean isVerified;

  @JsonProperty("Salary")
  private Long salary;
}
