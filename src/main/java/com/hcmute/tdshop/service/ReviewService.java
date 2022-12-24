package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface ReviewService {
  DataResponse rateProduct(AddReviewRequest request);
  DataResponse verifyReview(long id);
}
