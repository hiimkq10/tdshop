package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.CancelOrderDto;
import com.hcmute.tdshop.dto.shipservices.CreateOrderDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ShipService;
import com.hcmute.tdshop.service.shipservices.ShipServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ship")
public class ShipController {
  @Autowired
  ShipService shipService;

  @Autowired
  ShipServices shipServices;

  @GetMapping("/get-all")
  public DataResponse getAll() {
    return shipService.getAll();
  }

  @PostMapping("/calculate-fee")
  public DataResponse calculateFee(@RequestBody CalculateFeeDto dto) {
    return shipServices.calculateFee(dto);
  }

  @PostMapping("/create-order")
  public DataResponse createOrder(@RequestBody CreateOrderDto dto) {
    return shipServices.createOrder(dto);
  }

  @PostMapping("/cancel-order")
  public DataResponse createOrder(@RequestBody CancelOrderDto dto) {
    return shipServices.cancelOrder(dto);
  }
}
