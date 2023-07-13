package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.PaymentMethodRepository;
import com.hcmute.tdshop.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

  @Autowired
  PaymentMethodRepository paymentMethodRepository;

  @Override
  public DataResponse getAll() {
    return new DataResponse(paymentMethodRepository.findAll());
  }
}
