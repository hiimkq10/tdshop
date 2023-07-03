package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.address.AddAddressRequest;
import com.hcmute.tdshop.dto.address.AddressResponse;
import com.hcmute.tdshop.dto.address.UpdateAddressRequest;
import com.hcmute.tdshop.dto.location.DistrictResponse;
import com.hcmute.tdshop.dto.location.ProvinceResponse;
import com.hcmute.tdshop.dto.location.WardsResponse;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.District;
import com.hcmute.tdshop.entity.Province;
import com.hcmute.tdshop.entity.Wards;
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

  public ProvinceResponse ProvinceToProvinceResponse(Province province) {
    if (province == null) {
      return null;
    }
    ProvinceResponse provinceResponse = new ProvinceResponse();
    provinceResponse.setId(province.getId());
    provinceResponse.setName(province.getName());
    provinceResponse.setShortName(province.getShortName());
    provinceResponse.setType(province.getType());
    provinceResponse.setTypePriority(province.getTypePriority());

    return provinceResponse;
  }

  public DistrictResponse DistrictToDistrictResponse(District district) {
    if (district == null) {
      return null;
    }
    DistrictResponse districtResponse = new DistrictResponse();
    districtResponse.setId(district.getId());
    districtResponse.setName(district.getName());
    districtResponse.setShortName(district.getShortName());
    districtResponse.setType(district.getType());
    districtResponse.setTypePriority(district.getTypePriority());
    districtResponse.setProvince(ProvinceToProvinceResponse(district.getProvince()));

    return districtResponse;
  }

  public WardsResponse WardsToWardsResponse(Wards wards) {
    if (wards == null) {
      return null;
    }
    WardsResponse wardsResponse = new WardsResponse();
    wardsResponse.setId(wards.getId());
    wardsResponse.setName(wards.getName());
    wardsResponse.setShortName(wards.getShortName());
    wardsResponse.setType(wards.getType());
    wardsResponse.setTypePriority(wards.getTypePriority());

    return wardsResponse;
  }
}
