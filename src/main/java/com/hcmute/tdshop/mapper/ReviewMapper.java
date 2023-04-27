package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.dto.review.ReviewDto;
import com.hcmute.tdshop.dto.user.UserResponse;
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

  @Autowired
  ProductMapper productMapper;

  @Autowired
  UserMapper userMapper;

  public abstract Review AddReviewDtoToReview(AddReviewRequest request);

  public Product LongToProduct(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new RuntimeException(ApplicationConstants.PRODUCT_NOT_FOUND));
  }

  public abstract ReviewDto ReviewToReviewDto(Review review);
  public SimpleProductDto ProductToSimpleProductDto(Product product) {
    return productMapper.ProductToSimpleProductDto(product);
  }

  public UserResponse UserToUserResponse(User user) {
    return userMapper.UserToUserResponse(user);
  }
}
