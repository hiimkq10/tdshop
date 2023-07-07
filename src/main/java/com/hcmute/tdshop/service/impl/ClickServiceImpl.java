package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.product.AddClickRequest;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.entity.UserClick;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.UserClickRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.ClickService;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClickServiceImpl implements ClickService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  UserClickRepository userClickRepository;

  @Override
  public DataResponse addClick(AddClickRequest dto) {
    long id = AuthenticationHelper.getCurrentLoggedInUserId();
    Optional<Product> productOptional = productRepository.findByIdAndDeletedAtNull(dto.getProductId());
    if (!productOptional.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
    }
    Optional<User> userOptional = userRepository.findByIdAndDeletedAtIsNull(id);
    if (!userOptional.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR, ApplicationConstants.BAD_REQUEST_CODE);
    }
    LocalDateTime now = LocalDateTime.now();
    UserClick userClick = new UserClick(null, productOptional.get(), userOptional.get(), null, now);
    userClickRepository.save(userClick);
    return DataResponse.SUCCESSFUL;
  }
}
