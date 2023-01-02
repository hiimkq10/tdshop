package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.Review;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {
  @Autowired
  ProductRepository productRepository;

  @Autowired
  UserRepository userRepository;

  public abstract Review AddReviewDtoToReview(AddReviewRequest request);

  public Product LongToProduct(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new RuntimeException(ApplicationConstants.PRODUCT_NOT_FOUND));
  }
}
