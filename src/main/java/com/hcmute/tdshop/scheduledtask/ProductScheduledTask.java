package com.hcmute.tdshop.scheduledtask;

import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.OrderStatus;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.enums.OrderStatusEnum;
import com.hcmute.tdshop.repository.OrderStatusRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
      while (orderDetailIterator.hasNext()) {
        OrderDetail orderDetail = orderDetailIterator.next();
        orderDetail.getProduct().setTotal(orderDetail.getProduct().getTotal() + orderDetail.getQuantity());
        listOfProducts.add(orderDetail.getProduct());
      }
      order.setOrderStatus(status);
    }
    shopOrderRepository.saveAllAndFlush(orders);
    productRepository.saveAllAndFlush(listOfProducts);
  }
}
