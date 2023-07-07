package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.product.AddClickRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface ClickService {
  DataResponse addClick(AddClickRequest dto);
}
