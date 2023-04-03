package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.dto.security.UserInfo;
import com.hcmute.tdshop.dto.statistic.RatingDto;
import com.hcmute.tdshop.entity.Review;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.mapper.ReviewMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.OrderDetailRepository;
import com.hcmute.tdshop.repository.ReviewRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.security.model.CustomUserDetails;
import com.hcmute.tdshop.service.ReviewService;
import com.hcmute.tdshop.specification.ReviewSpecification;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReviewMapper reviewMapper;

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Override
  public DataResponse getAll(Pageable page) {
    Page<Review> pageOfReviews = reviewRepository.findAll(page);
    return new DataResponse(pageOfReviews);
  }

  @Override
  public DataResponse searchReview(long userId, long productId, String fromDate, Boolean isVerified, Boolean isValid,
      Pageable page) {
    List<Specification<Review>> specifications = new ArrayList<>();
    if (userId > 0) {
      specifications.add(ReviewSpecification.hasUser(userId));
    }
    if (productId > 0) {
      specifications.add(ReviewSpecification.hasProduct(productId));
    }
    if (fromDate != null) {
      specifications.add(ReviewSpecification.fromDateTime(Helper.MyLocalDateTimeParser(fromDate)));
    }
    if (isVerified != null) {
      specifications.add(ReviewSpecification.isVerified(isVerified));
    }
    if (isValid != null) {
      specifications.add(ReviewSpecification.isValid(isValid));
    }
    Specification<Review> conditions = SpecificationHelper.and(specifications);
    Page<Review> pageOfReviews = reviewRepository.findAll(conditions, page);
    return new DataResponse(pageOfReviews);
  }

  @Override
  public DataResponse addReview(AddReviewRequest request) {
    Review review = reviewMapper.AddReviewDtoToReview(request);
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    if (!checkIfUserBoughtProduct(request.getProduct(), userId)) {
      return DataResponse.BAD_REQUEST;
    }
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.USER_NOT_FOUND));
    review.setUser(user);
    review.setValid(false);
    review.setVerified(false);
    review.setCreatedAt(LocalDateTime.now());
    review = reviewRepository.save(review);
    return new DataResponse(ApplicationConstants.REVIEW_ADD_SUCCESSFULLY, review);
  }

  @Override
  public DataResponse deleteReview(long id) {
    Review review = reviewRepository.findById(id)
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.REVIEW_NOT_FOUND));
    reviewRepository.delete(review);
    return new DataResponse(ApplicationConstants.REVIEW_DELETE_SUCCESSFULLY);
  }

  @Override
  public DataResponse acceptReview(long id) {
    Review review = reviewRepository.findById(id)
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.REVIEW_NOT_FOUND));
    review.setVerified(true);
    review.setValid(true);
    review = reviewRepository.saveAndFlush(review);
    return new DataResponse(ApplicationConstants.REVIEW_VERIFY_SUCCESSFULLY, review);
  }

  @Override
  public DataResponse denyReview(long id) {
    Review review = reviewRepository.findById(id)
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.REVIEW_NOT_FOUND));
    review.setVerified(true);
    review.setValid(false);
    review = reviewRepository.saveAndFlush(review);
    return new DataResponse(ApplicationConstants.REVIEW_VERIFY_SUCCESSFULLY, review);
  }

  @Override
  public DataResponse checkUserBoughtProduct(long productId) {
    UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return new DataResponse(checkIfUserBoughtProduct(productId, user.getId()));
  }

  @Override
  public DataResponse getProductAvgReview(long productId) {
    List<Specification<Review>> specifications = new ArrayList<>();
    specifications.add(ReviewSpecification.hasProduct(productId));
    specifications.add(ReviewSpecification.isVerified(true));
    specifications.add(ReviewSpecification.isValid(true));
    Specification<Review> conditions = SpecificationHelper.and(specifications);
    List<Review> reviews = reviewRepository.findAll(conditions);
    double total = 0;
    long size = reviews.size();
    for (int i = 0; i < size; i++) {
      total = total + reviews.get(i).getRatingValue();
    }
    return new DataResponse(new RatingDto(productId, null, total / size, size));
  }

  public boolean checkIfUserBoughtProduct(long productId, long userId) {
    return orderDetailRepository.existsByProduct_IdAndOrder_User_Id(productId, userId);
  }

}
