package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.dto.review.ReviewDto;
import com.hcmute.tdshop.entity.Review;
import com.hcmute.tdshop.model.DataResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  DataResponse getAll(Pageable page);

  DataResponse searchReview(long userId, long productId, String fromDate, String toDateTime, Boolean isVerified, Boolean isValid, Pageable page);
  DataResponse searchAll(long userId, long productId, String fromDate, String toDateTime, Boolean isVerified, Boolean isValid);
  List<ReviewDto> searchAllList(long userId, long productId, String fromDate, String toDateTime, Boolean isVerified, Boolean isValid);
  DataResponse addReview(AddReviewRequest request);

  DataResponse deleteReview(long id);

  DataResponse acceptReview(long id);

  DataResponse denyReview(long id);

  DataResponse checkUserBoughtProduct(long productId);
  DataResponse getProductAvgReview(long productId);
  DataResponse addReviewForMobile(AddReviewRequest request);
}
