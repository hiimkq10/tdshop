package com.hcmute.tdshop.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAddressRequest {

  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.ADDRESS_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("Email")
  @Email(message = ApplicationConstants.ADDRESS_EMAIL_FORMAT_INVALID)
  @Size(max = 100, message = ApplicationConstants.ADDRESS_EMAIL_SIZE_INVALID)
  private String email;

  @JsonProperty("Phone")
  @Size(max = 10, message = ApplicationConstants.ADDRESS_PHONE_SIZE_INVALID)
  private String phone;

  @JsonProperty("AddressDetail")
  @Size(max = 150, message = ApplicationConstants.ADDRESS_DETAIL_SIZE_INVALID)
  private String addressDetail;

  @JsonProperty("IsDefault")
  private Boolean isDefault;

  @JsonProperty("WardsId")
  @PositiveOrZero(message = ApplicationConstants.ADDRESS_WARDS_ID_INVALID)
  private long wardsId;
}
