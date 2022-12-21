package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.promotion.AddPromotionRequest;
import com.hcmute.tdshop.dto.promotion.UpdatePromotionRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
  DataResponse getPromotion(Pageable pageable);
  DataResponse insertPromotion(AddPromotionRequest request);
  DataResponse updatePromotion(Long id, UpdatePromotionRequest request);
  DataResponse deletePromotion(Long id);
}
