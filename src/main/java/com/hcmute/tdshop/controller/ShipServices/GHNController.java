package com.hcmute.tdshop.controller.ShipServices;

import com.hcmute.tdshop.dto.shipservices.CalculateDeliveryTimeRequest;
import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.CancelOrderRequest;
import com.hcmute.tdshop.dto.shipservices.CreateOrderRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ShipService;
import com.hcmute.tdshop.service.shipservices.ShipServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PostMapping("/calculate-fee")
  public DataResponse calculateFee(@RequestBody CalculateFeeDto dto) {
    return ghnShipServices.calculateFee(dto);
  }

  @PostMapping("/calculate-delivery-time")
  public DataResponse calculateDeliveryTime(@RequestBody CalculateDeliveryTimeRequest dto) {
    return ghnShipServices.calculateExpectedDeliveryTime(dto);
  }

  @PostMapping("/create-order")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse createOrder(@RequestBody CreateOrderRequest dto) {
    return ghnShipServices.createOrder(dto);
  }

  @PostMapping("/cancel-order")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse cancleOrder(@RequestBody CancelOrderRequest dto) {
    return ghnShipServices.cancelOrder(dto);
  }

}
