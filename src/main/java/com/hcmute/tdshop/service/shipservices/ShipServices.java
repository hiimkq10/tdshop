package com.hcmute.tdshop.service.shipservices;

import com.hcmute.tdshop.dto.shipservices.CalculateDeliveryTimeRequest;
import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.CancelOrderRequest;
import com.hcmute.tdshop.dto.shipservices.CheckShipConditionDto;
import com.hcmute.tdshop.dto.shipservices.CreateOrderRequest;
import com.hcmute.tdshop.dto.shipservices.OrderSize;
import com.hcmute.tdshop.dto.shipservices.ShipOrderDto;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.mapper.OrderMapper;
import com.hcmute.tdshop.mapper.ShipServicesMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AddressRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShipDataRepository;
import com.hcmute.tdshop.repository.ShipRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.repository.WardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public abstract class ShipServices {

  @Autowired
  ShopOrderRepository shopOrderRepository;

  @Autowired
  ShipRepository shipRepository;

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ShipServicesMapper shipServicesMapper;

  @Autowired
  ShipDataRepository shipDataRepository;

  @Autowired
  WardsRepository wardsRepository;

  @Autowired
  OrderMapper orderMapper;

  @Value("${td-shop.lat}")
  String shopLat;

  @Value("${td-shop.lng}")
  String shopLng;

  @Value("${td-shop.address-detail}")
  String shopAddressDetail;

  @Value("${td-shop.name}")
  String shopName;

  @Value("${td-shop.phone}")
  String shopPhone;

  @Value("${td-shop.order.progress-time}")
  int orderProgressTime;

  @Value("${td-shop.location.province-id}")
  long shopProvinceId;

  @Value("${td-shop.location.district-id}")
  long shopDistrictId;

  @Value("${td-shop.location.wards-id}")
  long shopWardsId;

  public abstract boolean checkSize(ShopOrder order);
  public abstract boolean checkProductSize(OrderSize orderSize);

  public abstract boolean checkAllowCancelOrder(String statusCode);

  public abstract boolean checkCODAmount(ShopOrder order);

  public abstract boolean checkRegion(Address address);

  public abstract ShipOrderDto getShipOrder(ShopOrder order);

  public abstract DataResponse calculateFee(CalculateFeeDto dto);

  public abstract DataResponse createOrder(CreateOrderRequest dto);

  public abstract DataResponse cancelOrder(CancelOrderRequest dto);

  public abstract DataResponse calculateExpectedDeliveryTime(CalculateDeliveryTimeRequest dto);

  public abstract CheckShipConditionDto checkShipCondition(ShopOrder order);

//  @Autowired
//  GHNShipService ghnShipService;
//
//  @Autowired
//  LalamoveShipService lalamoveShipService;

//  public boolean checkProductSize(Long shipId, OrderSize orderSize) {
//    if (shipId == 2) {
//      return ghnShipService.checkProductsSize(orderSize);
//    }
//    return true;
//  }
//
//  public boolean checkAllowCancelOrder(Long shipId, String statusCode) {
//    if (shipId == 2) {
//      return ghnShipService.checkAllowCancelOrder(statusCode);
//    }
//    if (shipId == 3) {
//      return lalamoveShipService.checkAllowCancelOrder(statusCode);
//    }
//    return false;
//  }
//
//  public boolean checkCODAmount(Long shipId, Set<OrderDetail> setOfOrderDetails) {
//    if (shipId == 2) {
//      return ghnShipService.checkCodAmount(setOfOrderDetails);
//    }
//    if (shipId == 3) {
//      return false;
//    }
//    return true;
//  }
//
//  public boolean checkRegion(Long shipId, Address address) {
//    if (shipId == 3) {
//      return lalamoveShipService.checkRegion(address);
//    }
//    return true;
//  }
//
//  public ShipOrderDto getOrder(ShopOrder order) {
//    if (order.getShip().getId() == 2) {
//      return ghnShipService.getShipOrderDto(order);
//    }
//    if (order.getShip().getId() == 3) {
//      return lalamoveShipService.getShipOrderDto(order);
//    }
//    return null;
//  }
//
//  public DataResponse calculateFee(CalculateFeeDto dto) {
//    Optional<Ship> optionalData = shipRepository.findById(dto.getShipId());
//    if (!optionalData.isPresent()) {
//      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR, ApplicationConstants.BAD_REQUEST_CODE);
//    }
//    if (dto.getShipId() == 2) {
//      return ghnShipService.calculateFee(dto);
//    }
//    if (dto.getShipId() == 3) {
//      return lalamoveShipService.calculateFee(dto);
//    }
//    Ship ship = optionalData.get();
//    return new DataResponse(ship.getPrice());
//  }
//
//  public DataResponse createOrder(CreateOrderDto dto){
//    Optional<ShopOrder> optionalData = shopOrderRepository.findById(dto.getOrderId());
//    if (!optionalData.isPresent()) {
//      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
//    }
//    ShopOrder order = optionalData.get();
//
//    // Check order valid
//    if (!checkRegion(order.getShip().getId(), order.getAddress())){
//      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_REGION_NOT_SUPPORT, ApplicationConstants.ORDER_REGION_NOT_SUPPORT_CODE);
//    }
//    if (!checkCODAmount(order.getShip().getId(), order.getSetOfOrderDetails())) {
//      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT, ApplicationConstants.ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT_CODE);
//    }
//
//    OrderSize orderSize = new OrderSize(dto.getLength(), dto.getWidth(), dto.getHeight(), dto.getWeight());
//    if (!checkProductSize(order.getShip().getId(), orderSize)) {
//      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_COD_SIZE_EXCEED_SUPPORT_SIZE, ApplicationConstants.ORDER_COD_SIZE_EXCEED_SUPPORT_SIZE_CODE);
//    }
//
//    if (order.getShip().getId() == 2) {
//      return ghnShipService.createOrder(order, orderSize);
//    }
//    if (order.getShip().getId() == 3) {
//      return lalamoveShipService.createOrder(order, orderSize);
//    }
//    return new DataResponse(false);
//  }
//
//  public DataResponse cancelOrder(CancelOrderDto dto) {
//    Optional<ShopOrder> optionalData = shopOrderRepository.findById(dto.getOrderId());
//    if (!optionalData.isPresent()) {
//      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
//    }
//    ShopOrder order = optionalData.get();
//    if (order.getShip().getId() == 2) {
//      return ghnShipService.cancelOrder(order);
//    }
//    if (order.getShip().getId() == 3) {
//      return lalamoveShipService.cancelOrder(order);
//    }
//    return new DataResponse(true);
//  }
}
