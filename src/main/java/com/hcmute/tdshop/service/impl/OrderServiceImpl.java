package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.order.AddOrderRequest;
import com.hcmute.tdshop.dto.order.OrderResponse;
import com.hcmute.tdshop.entity.Cart;
import com.hcmute.tdshop.entity.CartItem;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.OrderStatus;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.enums.OrderStatusEnum;
import com.hcmute.tdshop.enums.PaymentMethodEnum;
import com.hcmute.tdshop.mapper.OrderMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.CartItemRepository;
import com.hcmute.tdshop.repository.CartRepository;
import com.hcmute.tdshop.repository.OrderStatusRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.service.OrderService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderMapper orderMapper;

  @Autowired
  private ShopOrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderStatusRepository orderStatusRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Override
  public DataResponse getOrder(Pageable page) {
    Page<ShopOrder> pageOfOrders = orderRepository.findAll(page);
    Page<OrderResponse> pageOfOrderResponse = new PageImpl<>(
        pageOfOrders.getContent().stream().map(orderMapper::OrderToOrderResponse).collect(Collectors.toList()),
        page,
        pageOfOrders.getNumberOfElements()
    );
    return new DataResponse(pageOfOrderResponse);
  }

  @Override
  public DataResponse getUserOrder(long userId, Pageable page) {
    Page<ShopOrder> pageOfOrders = orderRepository.findByUser_Id(userId);
    Page<OrderResponse> pageOfOrderResponse = new PageImpl<>(
        pageOfOrders.getContent().stream().map(orderMapper::OrderToOrderResponse).collect(Collectors.toList()),
        page,
        pageOfOrders.getNumberOfElements()
    );
    return new DataResponse(pageOfOrderResponse);
  }

  @Override
  @Transactional
  public DataResponse insertOrder(AddOrderRequest request) {
    ShopOrder order = orderMapper.AddOrderRequestToOrder(request);
    Set<OrderDetail> setOfOrderDetails = order.getSetOfOrderDetails();
    List<Product> listOfProducts = new ArrayList<>();
    for (OrderDetail orderDetail : setOfOrderDetails) {
      Product product = orderDetail.getProduct();
      if (product.getTotal() >= orderDetail.getQuantity()) {
        product.setTotal(product.getTotal() - orderDetail.getQuantity());
        listOfProducts.add(product);
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_QUANTITY_NOT_ENOUGH,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    productRepository.saveAllAndFlush(listOfProducts);

    Cart cart = cartRepository.findByUser_Id(request.getUserId()).get();
    Set<CartItem> setOfCartItems = cart.getSetOfCartItems();
    setOfCartItems = setOfCartItems.stream().filter(cartItem -> {
      for (Product product : listOfProducts) {
        if (cartItem.getProduct().getId().equals(product.getId())) {
          return true;
        }
      }
      return false;
    }).collect(Collectors.toSet());
    cartItemRepository.deleteAll(setOfCartItems);

    if (!(order.getPaymentMethod().getId() == PaymentMethodEnum.COD.getId())) {
      order.setOrderStatus(orderStatusRepository.findById(OrderStatusEnum.PROCCESSING.getId()).get());
    } else {
      order.setOrderStatus(orderStatusRepository.findById(OrderStatusEnum.AWAITINGPAYMENT.getId()).get());
    }
    order = orderRepository.save(order);
    return new DataResponse(orderMapper.OrderToOrderResponse(order));
  }

  @Override
  public DataResponse changeOrderStatus(long orderId, long statusId) {
    Optional<ShopOrder> optionalShopOrder = orderRepository.findById(orderId);
    if (optionalShopOrder.isPresent()) {
      ShopOrder order = optionalShopOrder.get();
      Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(statusId);
      if (optionalOrderStatus.isPresent()) {
        order.setOrderStatus(optionalOrderStatus.get());
        order = orderRepository.saveAndFlush(order);
        return new DataResponse(orderMapper.OrderToOrderResponse(order));
      }
      else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_STATUS_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse cancelOrder(long orderId) {
    Optional<ShopOrder> optionalShopOrder = orderRepository.findById(orderId);
    if (optionalShopOrder.isPresent()) {
      ShopOrder order = optionalShopOrder.get();
      if (order.getOrderStatus().getId().equals(OrderStatusEnum.AWAITINGPAYMENT.getId())) {
        order.setOrderStatus(orderStatusRepository.findById(OrderStatusEnum.CANCELED.getId()).get());
        order.setDeletedAt(LocalDateTime.now());
        order = orderRepository.saveAndFlush(order);
        return new DataResponse(orderMapper.OrderToOrderResponse(order));
      }
      else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ONLY_AWAITING_PAYMENT_ORDER_CAN_BE_DELETED, ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }
}
