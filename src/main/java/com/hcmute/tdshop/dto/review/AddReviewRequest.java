package com.hcmute.tdshop.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class AddReviewRequest {
  @JsonProperty("Id")
  private Long id;

  @JsonProperty("RatingValue")
  private double ratingValue;

  @JsonProperty("Comment")
  private String comment;

  @JsonProperty("CreatedAt")
  private LocalDateTime createdAt;

  @JsonProperty("ProductId")
  private Long product;
}
