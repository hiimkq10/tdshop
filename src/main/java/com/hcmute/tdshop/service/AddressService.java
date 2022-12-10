package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.address.AddAddressRequest;
import com.hcmute.tdshop.dto.address.UpdateAddressRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface AddressService {
  public DataResponse getAddressByUser();
  public DataResponse insertAddress(AddAddressRequest request);
  public DataResponse updateAddress(long id, UpdateAddressRequest request);
  public DataResponse deleteAddress(long id);
}
