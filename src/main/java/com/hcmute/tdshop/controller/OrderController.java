package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.order.AddOrderRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.OrderService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @GetMapping("/")
  public DataResponse getOrder(Pageable page) {
    return orderService.getOrder(page);
  }

  @GetMapping("/{userId}")
  public DataResponse getUserOrder(@PathVariable(name = "userId") long id, Pageable page) {
    return orderService.getUserOrder(id, page);
  }

  @PostMapping("/add")
  public DataResponse insertOrder(@RequestBody @Valid AddOrderRequest request) {
    return orderService.insertOrder(request);
  }

  @DeleteMapping("/cancel")
  public DataResponse cancelOrder(@RequestBody long orderId) {
    return orderService.cancelOrder(orderId);
  }
}
