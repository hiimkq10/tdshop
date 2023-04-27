package com.hcmute.tdshop.dto.review;

import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.dto.user.UserResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
  private Long id;
  private double ratingValue;
  private String comment;
  private LocalDateTime createdAt;
  private boolean isVerified;
  private boolean isValid;
  private long userId;
  private UserResponse user;
  private SimpleProductDto product;
}
