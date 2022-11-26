package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.address.AddAddressRequest;
import com.hcmute.tdshop.dto.address.AddressResponse;
import com.hcmute.tdshop.dto.address.UpdateAddressRequest;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.entity.Wards;
import com.hcmute.tdshop.mapper.AddressMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AddressRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.service.AddressService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private AddressMapper addressMapper;

  @Autowired
  private WardsRepository wardsRepository;

  @Override
  public DataResponse getAddressByUserId(long id) {
    if (userRepository.existsById(id)) {
      List<Address> listOfAddresses = addressRepository.findByUser_Id(id);
      List<AddressResponse> listOfAddressResponses = listOfAddresses.stream()
          .map(addressMapper::AddressToAddressResponse).collect(
              Collectors.toList());
      return new DataResponse(listOfAddressResponses);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  @Transactional
  public DataResponse insertAddress(AddAddressRequest request) {
    Address address = addressMapper.AddAddressRequestToAddress(request);
    long userId = request.getUserId();
    long wardsId = request.getWardsId();
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      Optional<Wards> optionalWards = wardsRepository.findById(wardsId);
      if (optionalWards.isPresent()) {
        User user = optionalUser.get();
        Wards wards = optionalWards.get();
        address.setUser(user);
        address.setWards(wards);
        if (address.getIsDefault()) {
          List<Address> listOfAddresses = addressRepository.findByIsDefault(true);
          listOfAddresses.forEach(addressToUpdate -> addressToUpdate.setIsDefault(false));
          addressRepository.saveAllAndFlush(listOfAddresses);
        }
        address = addressRepository.save(address);
        AddressResponse addressResponse = addressMapper.AddressToAddressResponse(address);
        return new DataResponse(ApplicationConstants.ADDRESS_ADD_SUCCESSFULLY, addressResponse);
      }
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.WARDS_NOT_FOUND,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  @Transactional
  public DataResponse updateAddress(long id, UpdateAddressRequest request) {
    Address addressToUpdate = addressMapper.UpdateAddressRequestToAddress(request);
    long wardsId = request.getWardsId();
    Optional<Address> optionalAddress = addressRepository.findById(id);
    if (optionalAddress.isPresent()) {
      Address currentAddress = optionalAddress.get();
      if (addressToUpdate.getName() != null) {
        currentAddress.setName(addressToUpdate.getName());
      }
      if (addressToUpdate.getEmail() != null) {
        currentAddress.setEmail(addressToUpdate.getEmail());
      }
      if (addressToUpdate.getPhone() != null) {
        currentAddress.setPhone(addressToUpdate.getPhone());
      }
      if (addressToUpdate.getAddressDetail() != null) {
        currentAddress.setAddressDetail(addressToUpdate.getAddressDetail());
      }
      if (addressToUpdate.getIsDefault() != null) {
        Boolean newIsDafault = addressToUpdate.getIsDefault();
        Boolean currentIsDefault = currentAddress.getIsDefault();
        if (newIsDafault) {
          if (!currentIsDefault) {
            List<Address> listOfAddresses = addressRepository.findByIsDefault(true);
            listOfAddresses.forEach(address -> address.setIsDefault(false));
            addressRepository.saveAllAndFlush(listOfAddresses);
            currentAddress.setIsDefault(newIsDafault);
          }
        }
      }
      if (wardsId > 0 && wardsId != currentAddress.getWards().getId()) {
        Optional<Wards> optionalWards = wardsRepository.findById(wardsId);
        if (optionalWards.isPresent()) {
          Wards wards = optionalWards.get();
          currentAddress.setWards(wards);
        }
      }
      currentAddress = addressRepository.saveAndFlush(currentAddress);
      return new DataResponse(ApplicationConstants.ADDRESS_UPDATE_SUCCESSFULLY, addressMapper.AddressToAddressResponse(currentAddress));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ADDRESS_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse deleteAddress(long id) {
    if (addressRepository.existsById(id)) {
      addressRepository.deleteById(id);
      return new DataResponse(ApplicationConstants.ADDRESS_DELETE_SUCCESSFULLY, true);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ADDRESS_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }
}
