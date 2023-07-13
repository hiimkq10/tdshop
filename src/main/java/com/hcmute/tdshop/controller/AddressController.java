package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.address.AddAddressRequest;
import com.hcmute.tdshop.dto.address.UpdateAddressRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.AddressService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

  @Autowired
  private AddressService addressService;

  @GetMapping("/my-address")
  public DataResponse getAddressByUserId() {
    return addressService.getAddressByUser();
  }

  @PostMapping("/add")
  public DataResponse insertAddress(@RequestBody @Valid AddAddressRequest request) {
    return addressService.insertAddress(request);
  }

  @PutMapping("/update/{id}")
  public DataResponse updateAddress(@PathVariable(name = "id") long id,
      @RequestBody @Valid UpdateAddressRequest request) {
    return addressService.updateAddress(id, request);
  }

  @DeleteMapping("/delete/{id}")
  public DataResponse deleteAddress(@PathVariable(name = "id") long id) {
    return addressService.deleteAddress(id);
  }
}
