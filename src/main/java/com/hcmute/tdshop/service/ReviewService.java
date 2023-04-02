package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.model.DataResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  DataResponse getAll(Pageable page);

  DataResponse searchReview(long userId, long productId, String fromDate, Boolean isVerified, Boolean isValid, Pageable page);

  DataResponse addReview(AddReviewRequest request);

  DataResponse deleteReview(long id);

  DataResponse acceptReview(long id);

  DataResponse denyReview(long id);

  DataResponse checkUserBoughtProduct(long productId, long userId);
}
