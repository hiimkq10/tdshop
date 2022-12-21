package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.promotion.AddPromotionRequest;
import com.hcmute.tdshop.dto.promotion.UpdatePromotionRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.PromotionService;
import org.springframework.data.domain.Pageable;

public class PromotionServiceImpl implements PromotionService {

  @Override
  public DataResponse getPromotion(Pageable pageable) {
    return null;
  }

  @Override
  public DataResponse insertPromotion(AddPromotionRequest request) {
    return null;
  }

  @Override
  public DataResponse updatePromotion(Long id, UpdatePromotionRequest request) {
    return null;
  }

  @Override
  public DataResponse deletePromotion(Long id) {
    return null;
  }
}
