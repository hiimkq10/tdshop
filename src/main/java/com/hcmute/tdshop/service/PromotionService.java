package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.promotion.AddPromotionRequest;
import com.hcmute.tdshop.dto.promotion.UpdatePromotionRequest;
import com.hcmute.tdshop.model.DataResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
  DataResponse getAll(Pageable pageable);
  DataResponse getPromotion(Long id, String keyword, Double fromRate, Double toRate, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
  DataResponse getById(Long id);
  DataResponse insertPromotion(AddPromotionRequest request);
  DataResponse updatePromotion(Long id, UpdatePromotionRequest request);
  DataResponse deletePromotion(Long id);
}
