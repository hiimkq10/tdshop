package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ReviewRepository;
import com.hcmute.tdshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
  @Autowired
  private ReviewRepository reviewRepository;

  @Override
  public DataResponse rateProduct(AddReviewRequest request) {
    return null;
  }

  @Override
  public DataResponse verifyReview(long id) {
    return null;
  }
}
