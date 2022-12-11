package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-method")
public class PaymentMethodController {
  @Autowired
  PaymentMethodService paymentMethodService;

  @GetMapping("/get-all")
  public DataResponse getAll() {
    return paymentMethodService.getAll();
  }
}
