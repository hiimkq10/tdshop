package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.Subscription;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.SubscriptionRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.SubscriptionService;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  @Autowired
  SubscriptionRepository subscriptionRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ProductRepository productRepository;

  public boolean checkUserInvalid(Long userId) {
    if (userId == null || userId <= 0) {
      return true;
    }
    if (userRepository.existsByIdAndDeletedAtNullAndIsActiveTrue(userId)) {
      return false;
    }
    return true;
  }

  public boolean checkProductInvalid(Long productId) {
    if (productId == null || productId <= 0) {
      return true;
    }
    if (productRepository.existsByIdAndStatus_IdInAndDeletedAtNull(productId,
        new Long[]{ProductStatusEnum.ONSALE.getId()})) {
      return false;
    }
    return true;
  }

  @Override
  public DataResponse subscribe(Long productId) {
    Long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    ;
    if (checkUserInvalid(userId) || checkProductInvalid(productId)) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    User user = userRepository.findByIdAndDeletedAtNullAndIsActiveTrue(userId).get();
    Product product = productRepository.findByIdAndStatus_IdInAndDeletedAtNull(productId,
        new Long[]{ProductStatusEnum.ONSALE.getId()}).get();
    if (!subscriptionRepository.existsByUser_IdIsAndProduct_IdIs(userId, productId)) {
      Subscription subscription = new Subscription(null, user, product);
      subscriptionRepository.save(subscription);
    }
    return new DataResponse(ApplicationConstants.FOLLOW_SUCCESS, true);
  }

  @Override
  public DataResponse unSubscribe(Long productId) {
    Long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    if (checkUserInvalid(userId) || checkProductInvalid(productId)) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    User user = userRepository.findByIdAndDeletedAtNullAndIsActiveTrue(userId).get();
    Product product = productRepository.findByIdAndStatus_IdInAndDeletedAtNull(productId,
        new Long[]{ProductStatusEnum.ONSALE.getId()}).get();
    Optional<Subscription> optionalData = subscriptionRepository.findByUser_IdIsAndProduct_IdIs(userId, productId);
    optionalData.ifPresent(subscription -> subscriptionRepository.delete(subscription));
    return new DataResponse(ApplicationConstants.UN_FOLLOW_SUCCESS, true);
  }

  @Override
  public DataResponse checkFollow(Long productId) {
    Long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    if (subscriptionRepository.existsByUser_IdIsAndProduct_IdIs(userId, productId)) {
      return new DataResponse(true);
    }
    return new DataResponse(false);
  }
}
