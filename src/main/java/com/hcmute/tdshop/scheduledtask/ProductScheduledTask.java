package com.hcmute.tdshop.scheduledtask;

import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.OrderStatus;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.enums.OrderStatusEnum;
import com.hcmute.tdshop.enums.ShipEnum;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.OrderStatusRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.service.shipservices.ShipServices;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductScheduledTask {

  private static final Logger logger = LoggerFactory.getLogger(ProductScheduledTask.class);
  @Autowired
  AppProperties appProperties;

  @Autowired
  ShopOrderRepository shopOrderRepository;

  @Autowired
  OrderStatusRepository orderStatusRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  @Qualifier("GHNShipService")
  private ShipServices ghnShipServices;

  @Autowired
  @Qualifier("LalamoveShipService")
  private ShipServices lalamoveShipService;

  @Scheduled(fixedRate = 2700000, initialDelay = 1000)
  public void clearAwaitingPaymentOrder() {
    final Long awaitingPaymentTimeout = appProperties.getOrderAwaitingPaymentTimeOut();
    final Long paymentTimeout = appProperties.getOrderPaymentTimeOut();
    if (awaitingPaymentTimeout < paymentTimeout) {
      logger.error("Payment timeout config is incorrect");
      return;
    }
    final LocalDateTime now = LocalDateTime.now();
    LocalDateTime end = now.minusSeconds((awaitingPaymentTimeout - paymentTimeout) / 1000);
    Optional<OrderStatus> optionalData = orderStatusRepository.findById(OrderStatusEnum.CANCELED.getId());
    if (!optionalData.isPresent()) {
      logger.error("Order status config is incorrect");
      return;
    }
    List<Product> listOfProducts = new ArrayList<>();
    OrderStatus status = optionalData.get();
    List<ShopOrder> orders = shopOrderRepository.findAllByOrderedAtLessThanEqualAndOrderStatus_IdIsAndDeletedAtIsNull(
        end,
        OrderStatusEnum.AWAITINGPAYMENT.getId());
    Iterator<ShopOrder> iterator = orders.iterator();
    while (iterator.hasNext()) {
      ShopOrder order = iterator.next();
      Iterator<OrderDetail> orderDetailIterator = order.getSetOfOrderDetails().iterator();
      int selAmount;
      while (orderDetailIterator.hasNext()) {
        OrderDetail orderDetail = orderDetailIterator.next();
        orderDetail.getProduct().setTotal(orderDetail.getProduct().getTotal() + orderDetail.getQuantity());
        selAmount = orderDetail.getProduct().getSelAmount() - orderDetail.getQuantity();
        orderDetail.getProduct().setSelAmount(Math.max(selAmount, 0));
        listOfProducts.add(orderDetail.getProduct());
      }
      order.setOrderStatus(status);
    }
    shopOrderRepository.saveAllAndFlush(orders);
    productRepository.saveAllAndFlush(listOfProducts);
  }

  @Scheduled(fixedRate = 2700000, initialDelay = 1000)
  public void updateOrderStatusByShipOrder() {
    List<Long> statusIds = Arrays.asList(
        OrderStatusEnum.DELIVERING.getId(),
        OrderStatusEnum.DELIVERED.getId()
    );
    List<ShopOrder> orders = shopOrderRepository.findAllByOrderStatus_IdInAndDeletedAtIsNull(statusIds);
    ShipServices shipServices;
    OrderStatus status;
    for (ShopOrder order : orders) {
      shipServices = getShipService(order.getShip().getId());
      if (shipServices != null) {
        OrderStatusEnum orderStatusEnum = shipServices.getShopOrderStatus(order);
        if (orderStatusEnum.getId() != order.getOrderStatus().getId()) {
          status = orderStatusRepository.findById(orderStatusEnum.getId()).orElse(null);
          if (status == null) {
            logger.error("Order status config is incorrect");
          }
          order.setOrderStatus(status);
        }
      }
    }
    shopOrderRepository.saveAllAndFlush(orders);
  }

  private ShipServices getShipService(Long shipId) {
    if (shipId == ShipEnum.FASTDELIVERY.getId()) {
      return ghnShipServices;
    }
    if (shipId == ShipEnum.LALAMOVE.getId()) {
      return lalamoveShipService;
    }
    return null;
  }
}
