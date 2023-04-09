package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.order.AddOrderRequest;
import com.hcmute.tdshop.dto.order.ChangeOrderStatusRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.OrderService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @GetMapping("/get-all")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse getOrder(Pageable page) {
    return orderService.getOrder(page);
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse searchOrder(
      @RequestParam(value = "order-id", required = false, defaultValue = "0") long orderId,
      @RequestParam(value = "status-id", required = false, defaultValue = "0") long statusId,
      Pageable page) {
    return orderService.searchOrder(orderId, statusId, page);
  }

  @GetMapping("/my-order")
  public DataResponse getUserOrder(
      @RequestParam(value = "order-id", required = false, defaultValue = "0") long orderId,
      @RequestParam(value = "status-id", required = false, defaultValue = "0") long statusId,
      Pageable page) {
    return orderService.getUserOrder(orderId, statusId, page);
  }

  @PostMapping("/add")
  public DataResponse insertOrder(@RequestBody @Valid AddOrderRequest request) {
    return orderService.insertOrder(request);
  }

  @DeleteMapping("/cancel")
  public DataResponse cancelOrder(@RequestParam(name = "order") long orderId) {
    return orderService.cancelOrder(orderId);
  }

  @PostMapping("/admin/change-status")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse changeOrderStatus(@RequestBody @Valid ChangeOrderStatusRequest request) {
    return orderService.changeOrderStatus(request);
  }

  @GetMapping("/register-client")
  public SseEmitter registerClient() {
    return orderService.registerClient();
  }

  @GetMapping("/send-message")
  public DataResponse sendMessage() {
    orderService.sendMessage(4L);
    return DataResponse.SUCCESSFUL;
  }
}
