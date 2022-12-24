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
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
  public DataResponse getAddressByUser() {
    long id = AuthenticationHelper.getCurrentLoggedInUserId();
    if (userRepository.existsById(id)) {
      List<Address> listOfAddresses = addressRepository.findByUser_IdAndDeletedAtIsNull(id);
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
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
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
          List<Address> listOfAddresses = addressRepository.findByIsDefaultAndUser_IdAndDeletedAtIsNull(true, userId);
          listOfAddresses.forEach(addressToUpdate -> addressToUpdate.setIsDefault(false));
          addressRepository.saveAllAndFlush(listOfAddresses);
        }
        else {
          if (!addressRepository.existsByIsDefaultAndUser_IdAndDeletedAtIsNull(true, userId)) {
            address.setIsDefault(true);
          }
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
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    Address addressToUpdate = addressMapper.UpdateAddressRequestToAddress(request);
    long wardsId = request.getWardsId();
    Optional<Address> optionalAddress = addressRepository.findByIdAndUser_IdAndDeletedAtIsNull(id, userId);
    if (optionalAddress.isPresent()) {
      Address currentAddress = optionalAddress.get();
      if (!Helper.checkIfStringIsBlank(addressToUpdate.getName())) {
        currentAddress.setName(addressToUpdate.getName());
      }
      if (!Helper.checkIfStringIsBlank(addressToUpdate.getEmail())) {
        currentAddress.setEmail(addressToUpdate.getEmail());
      }
      if (!Helper.checkIfStringIsBlank(addressToUpdate.getPhone())) {
        currentAddress.setPhone(addressToUpdate.getPhone());
      }
      if (!Helper.checkIfStringIsBlank(addressToUpdate.getAddressDetail())) {
        currentAddress.setAddressDetail(addressToUpdate.getAddressDetail());
      }
      if (addressToUpdate.getIsDefault() != null) {
        Boolean newIsDafault = addressToUpdate.getIsDefault();
        Boolean currentIsDefault = currentAddress.getIsDefault();
        if (newIsDafault) {
          if (!currentIsDefault) {
            List<Address> listOfAddresses = addressRepository.findByIsDefaultAndUser_IdAndDeletedAtIsNull(true, userId);
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
  @Transactional
  public DataResponse deleteAddress(long id) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    Optional<Address> optionalAddress = addressRepository.findByIdAndUser_IdAndDeletedAtIsNull(id, userId);
    if (optionalAddress.isPresent()) {
      Address address = optionalAddress.get();
      if (address.getIsDefault()) {
        List<Address> addresses = addressRepository.findByIsDefaultAndUser_IdAndDeletedAtIsNull(false, userId);
        if (addresses.size() > 0) {
          addresses.get(0).setIsDefault(true);
          addressRepository.saveAndFlush(addresses.get(0));
        }
      }
      address.setDeletedAt(LocalDateTime.now());
      address.setIsDefault(false);
      addressRepository.saveAndFlush(address);
      return new DataResponse(ApplicationConstants.ADDRESS_DELETE_SUCCESSFULLY, true);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ADDRESS_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }
}
