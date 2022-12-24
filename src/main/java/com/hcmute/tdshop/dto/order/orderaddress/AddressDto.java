package com.hcmute.tdshop.dto.order.orderaddress;

import lombok.Data;

@Data
public class AddressDto {
  private long id;
  private String name;
  private String email;
  private String phone;
  private String addressDetail;
  private WardsDto wards;
  private DistrictDto district;
  private ProvinceDto province;
}
