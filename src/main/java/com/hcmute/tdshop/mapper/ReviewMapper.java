package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {

  public abstract Review AddReviewDtoToReview(AddReviewRequest request);
}
