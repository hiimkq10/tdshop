package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.OrderService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentController {

  @Autowired
  OrderService orderService;

  @GetMapping("/success")
  @ResponseBody
  public DataResponse updateMomoPaymentOrder(HttpServletRequest request, HttpServletResponse response) {
    return orderService.updateMomoPaymentOrder(request, response);
  }
}
