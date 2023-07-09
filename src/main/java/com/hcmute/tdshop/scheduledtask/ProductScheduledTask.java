package com.hcmute.tdshop.scheduledtask;

import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.entity.Cart;
import com.hcmute.tdshop.entity.CartItem;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.OrderStatus;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.enums.OrderStatusEnum;
import com.hcmute.tdshop.enums.ShipEnum;
import com.hcmute.tdshop.repository.CartRepository;
import com.hcmute.tdshop.repository.OrderStatusRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.service.shipservices.ShipServices;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
  CartRepository cartRepository;

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

    List<Long> userIds = orders.stream().map(item -> item.getUser().getId()).collect(Collectors.toList());
    List<Cart> carts = cartRepository.findByUser_IdIn(userIds);
    Map<Long, Cart> cartMap = new HashMap<>();
    for (Cart cart : carts) {
      cartMap.put(cart.getUser().getId(), cart);
    }

    Cart cart = null;
    CartItem cartItem = null;
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

        if (cartMap.get(order.getUser().getId()) != null) {
          cart = cartMap.get(order.getUser().getId());
          cartItem = cart.getSetOfCartItems().stream().filter(item -> Objects.equals(item.getProduct().getId(),
              orderDetail.getProduct().getId())).findFirst().orElse(null);
          if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + orderDetail.getQuantity());
          } else {
            cartMap.get(order.getUser().getId()).getSetOfCartItems().add(
                new CartItem(null, orderDetail.getQuantity(), cartMap.get(order.getUser().getId()),
                    orderDetail.getProduct()));
          }
        }
      }
      order.setOrderStatus(status);
    }
    shopOrderRepository.saveAllAndFlush(orders);
    productRepository.saveAllAndFlush(listOfProducts);
    cartRepository.saveAllAndFlush(cartMap.values());
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
