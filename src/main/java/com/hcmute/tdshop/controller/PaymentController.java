package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.payment.momo.MomoPaymentResultDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.OrderService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentController {

  @Autowired
  OrderService orderService;

  @PostMapping("/success")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateMomoPaymentOrder(@RequestBody MomoPaymentResultDto momoPaymentResultDto) {
    System.out.println("momo");
    orderService.updateMomoPaymentOrder(momoPaymentResultDto);
  }
}
