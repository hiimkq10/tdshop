package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.payment.momo.MomoConfig;
import com.hcmute.tdshop.dto.payment.momo.MomoPaymentResultDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.payment.PaymentServiceImpl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentController {

  @Autowired
  PaymentServiceImpl paymentService;

  @Autowired
  MomoConfig momoConfig;

  @GetMapping("/success")
  public DataResponse testMomo(HttpServletRequest request) {
    System.out.println(new MomoPaymentResultDto(request));
    return new DataResponse("Payment Successfully");
  }
}
