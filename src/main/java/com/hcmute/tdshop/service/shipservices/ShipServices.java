package com.hcmute.tdshop.service.shipservices;

import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.CancelOrderDto;
import com.hcmute.tdshop.dto.shipservices.CreateOrderDto;
import com.hcmute.tdshop.dto.shipservices.OrderSize;
import com.hcmute.tdshop.dto.shipservices.ShipOrderDto;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.Ship;
import com.hcmute.tdshop.entity.ShipData;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ShipRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipServices {
  @Autowired
  ShopOrderRepository shopOrderRepository;

  @Autowired
  ShipRepository shipRepository;

  @Autowired
  GHNShipService ghnShipService;

  @Autowired
  LalamoveShipService lalamoveShipService;

  public boolean checkProductSize(Long shipId, OrderSize orderSize) {
    if (shipId == 2) {
      return ghnShipService.checkProductsSize(orderSize);
    }
    return true;
  }

  public boolean checkAllowCancelOrder(Long shipId, String statusCode) {
    if (shipId == 2) {
      return ghnShipService.checkAllowCancelOrder(statusCode);
    }
    if (shipId == 3) {
      return lalamoveShipService.checkAllowCancelOrder(statusCode);
    }
    return false;
  }

  public boolean checkCODAmount(Long shipId, Set<OrderDetail> setOfOrderDetails) {
    if (shipId == 2) {
      return ghnShipService.checkCodAmount(setOfOrderDetails);
    }
    if (shipId == 3) {
      return false;
    }
    return true;
  }

  public boolean checkRegion(Long shipId, Address address) {
    if (shipId == 3) {
      return lalamoveShipService.checkRegion(address);
    }
    return true;
  }

  public ShipOrderDto getOrder(ShopOrder order) {
    if (order.getShip().getId() == 2) {
      return ghnShipService.getShipOrderDto(order);
    }
    if (order.getShip().getId() == 3) {
      return lalamoveShipService.getShipOrderDto(order);
    }
    return null;
  }

  public DataResponse calculateFee(CalculateFeeDto dto) {
    Optional<Ship> optionalData = shipRepository.findById(dto.getShipId());
    if (!optionalData.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR, ApplicationConstants.BAD_REQUEST_CODE);
    }
    if (dto.getShipId() == 2) {
      return ghnShipService.calculateFee(dto);
    }
    if (dto.getShipId() == 3) {
      return lalamoveShipService.calculateFee(dto);
    }
    Ship ship = optionalData.get();
    return new DataResponse(ship.getPrice());
  }

  public DataResponse createOrder(CreateOrderDto dto){
    Optional<ShopOrder> optionalData = shopOrderRepository.findById(dto.getOrderId());
    if (!optionalData.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
    }
    ShopOrder order = optionalData.get();

    // Check order valid
    if (!checkRegion(order.getShip().getId(), order.getAddress())){
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_REGION_NOT_SUPPORT, ApplicationConstants.ORDER_REGION_NOT_SUPPORT_CODE);
    }
    if (!checkCODAmount(order.getShip().getId(), order.getSetOfOrderDetails())) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT, ApplicationConstants.ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT_CODE);
    }

    OrderSize orderSize = new OrderSize(dto.getLength(), dto.getWidth(), dto.getHeight(), dto.getWeight());
    if (!checkProductSize(order.getShip().getId(), orderSize)) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_COD_SIZE_EXCEED_SUPPORT_SIZE, ApplicationConstants.ORDER_COD_SIZE_EXCEED_SUPPORT_SIZE_CODE);
    }

    if (order.getShip().getId() == 2) {
      return ghnShipService.createOrder(order, orderSize);
    }
    if (order.getShip().getId() == 3) {
      return lalamoveShipService.createOrder(order, orderSize);
    }
    return new DataResponse(false);
  }

  public DataResponse cancelOrder(CancelOrderDto dto) {
    Optional<ShopOrder> optionalData = shopOrderRepository.findById(dto.getOrderId());
    if (!optionalData.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
    }
    ShopOrder order = optionalData.get();
    if (order.getShip().getId() == 2) {
      return ghnShipService.cancelOrder(order);
    }
    if (order.getShip().getId() == 3) {
      return lalamoveShipService.cancelOrder(order);
    }
    return new DataResponse(true);
  }
}
