package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.dto.review.ReviewDto;
import com.hcmute.tdshop.dto.security.UserInfo;
import com.hcmute.tdshop.dto.statistic.RatingWithStarDto;
import com.hcmute.tdshop.entity.Review;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.mapper.ReviewMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.OrderDetailRepository;
import com.hcmute.tdshop.repository.ReviewRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.ReviewService;
import com.hcmute.tdshop.specification.ReviewSpecification;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    return new DataResponse(pageOfReviews.map(reviewMapper::ReviewToReviewDto));
  }

  @Override
  public DataResponse searchReview(long userId, long productId, String fromDate, String toDate, Boolean isVerified,
      Boolean isValid,
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
    if (toDate != null) {
      specifications.add(ReviewSpecification.toDateTime(Helper.MyLocalDateTimeParser(toDate)));
    }
    if (isVerified != null) {
      specifications.add(ReviewSpecification.isVerified(isVerified));
    }
    if (isValid != null) {
      specifications.add(ReviewSpecification.isValid(isValid));
    }
    Specification<Review> conditions = SpecificationHelper.and(specifications);
    Page<Review> pageOfReviews = reviewRepository.findAll(conditions, page);
    return new DataResponse(pageOfReviews.map(reviewMapper::ReviewToReviewDto));
  }

  @Override
  public DataResponse searchAll(long userId, long productId, String fromDate, String toDate, Boolean isVerified,
      Boolean isValid) {
    return new DataResponse(searchAllList(userId, productId, fromDate, toDate, isVerified, isValid));
  }

  public List<ReviewDto> searchAllList(long userId, long productId, String fromDate, String toDate, Boolean isVerified,
      Boolean isValid) {
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
    if (toDate != null) {
      specifications.add(ReviewSpecification.toDateTime(Helper.MyLocalDateTimeParser(toDate)));
    }
    if (isVerified != null) {
      specifications.add(ReviewSpecification.isVerified(isVerified));
    }
    if (isValid != null) {
      specifications.add(ReviewSpecification.isValid(isValid));
    }
    Specification<Review> conditions = SpecificationHelper.and(specifications);
    List<Review> pageOfReviews = reviewRepository.findAll(conditions);
    return pageOfReviews.stream().map(reviewMapper::ReviewToReviewDto).collect(Collectors.toList());
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
    long star1 = 0;
    long star2 = 0;
    long star3 = 0;
    long star4 = 0;
    long star5 = 0;
    int ratingValue = 0;
    for (int i = 0; i < size; i++) {
      total = total + reviews.get(i).getRatingValue();
      if (reviews.get(i).getRatingValue() < 1.5) {
        star1 += 1;
      } else if (reviews.get(i).getRatingValue() >= 1.5 && reviews.get(i).getRatingValue() < 2.5) {
        star2 += 1;
      } else if (reviews.get(i).getRatingValue() >= 2.5 && reviews.get(i).getRatingValue() < 3.5) {
        star3 += 1;
      } else if (reviews.get(i).getRatingValue() >= 3.5 && reviews.get(i).getRatingValue() < 4.5) {
        star4 += 1;
      } else if (reviews.get(i).getRatingValue() >= 4.5) {
        star5 += 1;
      }
    }
    double avg = 0;
    if (size != 0 && total != 0) {
      avg = total / size;
    }
    return new DataResponse(new RatingWithStarDto(productId, null, avg, size, star1, star2, star3, star4, star5));
  }

  @Override
  public DataResponse addReviewForMobile(AddReviewRequest request) {
    Review review = reviewMapper.AddReviewDtoToReview(request);
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    if (!checkIfUserBoughtProduct(request.getProduct(), userId)) {
      return DataResponse.BAD_REQUEST;
    }
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.USER_NOT_FOUND));
    review.setUser(user);
    review.setValid(true);
    review.setVerified(true);
    review.setCreatedAt(LocalDateTime.now());
    review = reviewRepository.save(review);
    return new DataResponse(ApplicationConstants.REVIEW_ADD_SUCCESSFULLY, review);
  }

  public boolean checkIfUserBoughtProduct(long productId, long userId) {
    return orderDetailRepository.existsByProduct_IdAndOrder_User_Id(productId, userId);
  }

}
