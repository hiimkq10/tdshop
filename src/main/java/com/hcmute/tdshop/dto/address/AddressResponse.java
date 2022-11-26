package com.hcmute.tdshop.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddressResponse {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Email")
  private String email;

  @JsonProperty("Phone")
  private String phone;

  @JsonProperty("AddressDetail")
  private String addressDetail;

  @JsonProperty("IsDefault")
  private Boolean isDefault;

  @JsonProperty("ProvinceId")
  private Long provinceId;

  @JsonProperty("ProvinceName")
  private String provinceName;

  @JsonProperty("DistrictId")
  private Long districtId;

  @JsonProperty("DistrictName")
  private String districtName;

  @JsonProperty("WardsId")
  private Long wardsId;

  @JsonProperty("WardsName")
  private String wardsName;
}
