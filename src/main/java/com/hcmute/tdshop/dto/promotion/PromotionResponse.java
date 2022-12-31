package com.hcmute.tdshop.dto.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.entity.Category;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PromotionResponse {
  private Long id;
  private String name;
  private String description;
  private double discountRate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDate;

  @JsonProperty("Categories")
  private Set<PromotionCategoryDto> setOfCategories;
}
