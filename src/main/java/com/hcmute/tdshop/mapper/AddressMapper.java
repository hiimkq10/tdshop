package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.address.AddAddressRequest;
import com.hcmute.tdshop.dto.address.AddressResponse;
import com.hcmute.tdshop.dto.address.UpdateAddressRequest;
import com.hcmute.tdshop.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AddressMapper {
  public AddressResponse AddressToAddressResponse(Address address) {
    if (address == null) {
      return null;
    }
    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId(address.getId());
    addressResponse.setName(address.getName());
    addressResponse.setEmail(address.getEmail());
    addressResponse.setPhone(address.getPhone());
    addressResponse.setAddressDetail(address.getAddressDetail());
    addressResponse.setIsDefault(address.getIsDefault());
    addressResponse.setProvinceId(address.getWards().getDistrict().getProvince().getId());
    addressResponse.setProvinceName(address.getWards().getDistrict().getProvince().getName());
    addressResponse.setDistrictId(address.getWards().getDistrict().getId());
    addressResponse.setDistrictName(address.getWards().getDistrict().getName());
    addressResponse.setWardsId(address.getWards().getId());
    addressResponse.setWardsName(address.getWards().getName());
    addressResponse.setLat(address.getLat());
    addressResponse.setLng(address.getLng());

    return addressResponse;
  }

  public abstract Address AddAddressRequestToAddress(AddAddressRequest request);
  public abstract Address UpdateAddressRequestToAddress(UpdateAddressRequest request);
}
