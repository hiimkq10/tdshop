package com.hcmute.tdshop.controller.ShipServices;

import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.CancelOrderRequest;
import com.hcmute.tdshop.dto.shipservices.CreateOrderRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ShipService;
import com.hcmute.tdshop.service.shipservices.ShipServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ghn")
public class GHNController {
  @Autowired
  ShipService shipService;

  @Autowired
  @Qualifier("GHNShipService")
  private ShipServices ghnShipServices;

  @GetMapping("/get-all")
  public DataResponse getAll() {
    return shipService.getAll();
  }

  @PostMapping("/calculate-fee")
  public DataResponse calculateFee(@RequestBody CalculateFeeDto dto) {
    return ghnShipServices.calculateFee(dto);
  }

  @PostMapping("/create-order")
  public DataResponse createOrder(@RequestBody CreateOrderRequest dto) {
    return ghnShipServices.createOrder(dto);
  }

  @PostMapping("/cancel-order")
  public DataResponse createOrder(@RequestBody CancelOrderRequest dto) {
    return ghnShipServices.cancelOrder(dto);
  }
}
