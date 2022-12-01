package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.order.AddOrderRequest;
import com.hcmute.tdshop.dto.order.OrderDetailDto;
import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.dto.order.OrderResponse;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.PaymentMethod;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.Ship;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.repository.AddressRepository;
import com.hcmute.tdshop.repository.PaymentMethodRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShipRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private PaymentMethodRepository paymentMethodRepository;

  @Autowired
  private ShipRepository shipRepository;

  @Autowired
  private AddressRepository addressRepository;

  public ShopOrder AddOrderRequestToOrder(AddOrderRequest request) {
    if (request == null) {
      return null;
    }
    ShopOrder order = new ShopOrder();
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.USER_NOT_FOUND));
    PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.PAYMENT_NOT_FOUND));
    Ship ship = shipRepository.findById(request.getShipId())
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.SHIP_NOT_FOUND));
    Set<Product> setOfProduct = productRepository.findByIdIn(
        request.getSetOfProducts().stream().map(OrderProductDto::getProductId).collect(Collectors.toSet()));
    if (setOfProduct.size() != request.getSetOfProducts().size()) {
      throw new RuntimeException(ApplicationConstants.PRODUCT_NOT_FOUND);
    }
    Address address = addressRepository.findById(request.getAddressId())
        .orElseThrow(() -> new RuntimeException(ApplicationConstants.ADDRESS_NOT_FOUND));

    HashMap<Long, Integer> productQuantity = new HashMap<>();
    for (OrderProductDto productDto : request.getSetOfProducts()) {
      productQuantity.put(productDto.getProductId(), productDto.getQuantity());
    }

    Set<OrderDetail> setOfOrderDetails = new HashSet<>();
    for (Product product : setOfProduct) {
      setOfOrderDetails.add(new OrderDetail(
          null,
          product.getPrice(),
          product.getProductPromotion().getDiscountRate(),
          product.getPrice() * (1 - product.getProductPromotion().getDiscountRate()),
          productQuantity.get(product.getId()),
          product,
          null
      ));
    }

    order.setOrderedAt(LocalDateTime.now());
    order.setUser(user);
    order.setPaymentMethod(paymentMethod);
    order.setShip(ship);
    order.setAddress(address);
    order.setSetOfOrderDetails(setOfOrderDetails);

    return order;
  }

  public OrderResponse OrderToOrderResponse(ShopOrder order) {
    if (order == null) {
      return null;
    }

    OrderResponse orderResponse = new OrderResponse();
    orderResponse.setId(order.getId());
    orderResponse.setOrderedAt(order.getOrderedAt());
    orderResponse.setPaymentMethod(order.getPaymentMethod());
    orderResponse.setShip(order.getShip());
    orderResponse.setOrderStatus(order.getOrderStatus());
    orderResponse.setAddress(order.getAddress());
    orderResponse.setSetOfOrderDetailDtos(SetOfOrderDetailsToSetOfOrderDetailDtos(order.getSetOfOrderDetails()));

    return orderResponse;
  }

  public Set<OrderDetailDto> SetOfOrderDetailsToSetOfOrderDetailDtos(Set<OrderDetail> setOfOrderDetails) {
    if (setOfOrderDetails == null) {
      return null;
    }

    Set<OrderDetailDto> setOfOrderDetailDtos = new LinkedHashSet<>();
    for (OrderDetail orderDetail : setOfOrderDetails) {
      setOfOrderDetailDtos.add(OrderDetailToOrderDetailDto(orderDetail));
    }
    return setOfOrderDetailDtos;
  }

  public OrderDetailDto OrderDetailToOrderDetailDto(OrderDetail orderDetail) {
    if (orderDetail == null) {
      return null;
    }
    OrderDetailDto orderDetailDto = new OrderDetailDto();
    orderDetailDto.setId(orderDetail.getId());
    orderDetailDto.setPrice(orderDetail.getPrice());
    orderDetailDto.setDiscountRate(orderDetail.getDiscountRate());
    orderDetailDto.setFinalPrice(orderDetail.getFinalPrice());
    orderDetailDto.setQuantity(orderDetail.getQuantity());
    orderDetailDto.setProductId(orderDetail.getProduct().getId());
    orderDetailDto.setSku(orderDetail.getProduct().getSku());
    orderDetailDto.setName(orderDetail.getProduct().getName());
    orderDetailDto.setImageUrl(orderDetail.getProduct().getImageUrl());

    return orderDetailDto;
  }
}
