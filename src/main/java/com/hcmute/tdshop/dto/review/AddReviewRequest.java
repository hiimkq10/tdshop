package com.hcmute.tdshop.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.entity.User;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AddReviewRequest {

  @JsonProperty("RatingValue")
  private double ratingValue;

  @JsonProperty("Comment")
  private String comment;

  @JsonProperty("ProductId")
  private Long product;
}
