package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;

public interface SubscriptionService {

  DataResponse subscribe(Long productId);

  DataResponse unSubscribe(Long productId);
  DataResponse checkFollow(Long productId);
}
