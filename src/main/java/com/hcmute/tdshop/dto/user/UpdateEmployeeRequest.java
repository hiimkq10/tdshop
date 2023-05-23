package com.hcmute.tdshop.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.annotations.ValueOfLocalDate;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {

  @JsonProperty("FirstName")
  @Size(max = 50, message = ApplicationConstants.USER_FIRST_NAME_SIZE_INVALID)
  private String firstName;

  @JsonProperty("LastName")
  @Size(max = 50, message = ApplicationConstants.USER_LAST_NAME_SIZE_INVALID)
  private String lastName;

  @JsonProperty("Birthdate")
  @ValueOfLocalDate()
  private String birthdate;

  @JsonProperty("Gender")
  private Boolean gender;

  @JsonProperty("Salary")
  private Long salary;
}
