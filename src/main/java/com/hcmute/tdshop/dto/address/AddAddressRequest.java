package com.hcmute.tdshop.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddAddressRequest {

  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.ADDRESS_NAME_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.ADDRESS_NAME_MISSING)
  private String name;

  @JsonProperty("Email")
  @Email(message = ApplicationConstants.ADDRESS_EMAIL_FORMAT_INVALID)
  @Size(max = 100, message = ApplicationConstants.ADDRESS_EMAIL_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.ADDRESS_EMAIL_MANDATORY)
  private String email;

  @JsonProperty("Phone")
  @Size(max = 10, message = ApplicationConstants.ADDRESS_PHONE_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.ADDRESS_PHONE_MANDATORY)
  private String phone;

  @JsonProperty("AddressDetail")
  @Size(max = 150, message = ApplicationConstants.ADDRESS_DETAIL_SIZE_INVALID)
  @NotBlank(message = ApplicationConstants.ADDRESS_DETAIL_MISSING)
  private String addressDetail;

  @JsonProperty("IsDefault")
  @NotNull
  private Boolean isDefault;

  @JsonProperty("WardsId")
  @Positive(message = ApplicationConstants.ADDRESS_WARDS_ID_INVALID)
  private long wardsId;

  @JsonProperty("UserId")
  @Positive(message = ApplicationConstants.ADDRESS_USER_ID_INVALID)
  private long userId;
}
