package com.hcmute.tdshop.service.impl;

import com.google.gson.Gson;
import com.hcmute.tdshop.dto.order.AddOrderRequest;
import com.hcmute.tdshop.dto.order.ChangeOrderStatusRequest;
import com.hcmute.tdshop.dto.order.OrderResponse;
import com.hcmute.tdshop.dto.payment.momo.MomoConfig;
import com.hcmute.tdshop.dto.payment.momo.MomoPaymentResponse;
import com.hcmute.tdshop.dto.payment.momo.MomoPaymentResultDto;
import com.hcmute.tdshop.dto.serversentevent.Clients;
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
import com.hcmute.tdshop.repository.OrderDetailRepository;
import com.hcmute.tdshop.repository.OrderStatusRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.service.OrderService;
import com.hcmute.tdshop.service.payment.PaymentServiceImpl;
import com.hcmute.tdshop.specification.OrderSpecification;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
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

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Autowired
  private PaymentServiceImpl paymentService;

  @Autowired
  private MomoConfig momoConfig;

  private Clients clients = new Clients();
  private Gson gson = new Gson();

  @Override
  public DataResponse getOrder(Pageable page) {
    Page<ShopOrder> pageOfOrders = orderRepository.findByDeletedAtIsNull(page);
    Page<OrderResponse> pageOfOrderResponse = new PageImpl<>(
        pageOfOrders.getContent().stream().map(orderMapper::OrderToOrderResponse).collect(Collectors.toList()),
        page,
        pageOfOrders.getTotalElements()
    );
    return new DataResponse(pageOfOrderResponse);
  }

  @Override
  public DataResponse searchOrder(Long orderId, Long statusId, Pageable page) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();

    List<Specification<ShopOrder>> specifications = new ArrayList<>();
    specifications.add(OrderSpecification.isNotDeleted());
    if (orderId > 0) {
      specifications.add(OrderSpecification.hasId(orderId));
    }
    if (statusId > 0) {
      specifications.add(OrderSpecification.hasStatus(statusId));
    }

    Specification<ShopOrder> conditions = SpecificationHelper.and(specifications);
    Page<ShopOrder> pageOfOrders = orderRepository.findAll(conditions, page);
    Page<OrderResponse> pageOfOrderResponse = new PageImpl<>(
        pageOfOrders.getContent().stream().map(orderMapper::OrderToOrderResponse).collect(Collectors.toList()),
        page,
        pageOfOrders.getTotalElements()
    );
    return new DataResponse(pageOfOrderResponse);
  }

  @Override
  public DataResponse getUserOrder(Long orderId, Long status, Pageable page) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();

    List<Specification<ShopOrder>> specifications = new ArrayList<>();
    specifications.add(OrderSpecification.hasUser(userId));
    specifications.add(OrderSpecification.isNotDeleted());
    if (orderId > 0) {
      specifications.add(OrderSpecification.hasId(orderId));
    }
    if (status > 0) {
      specifications.add(OrderSpecification.hasStatus(status));
    }

    Specification<ShopOrder> conditions = SpecificationHelper.and(specifications);
    Page<ShopOrder> pageOfOrders = orderRepository.findAll(conditions, page);
    Page<OrderResponse> pageOfOrderResponse = new PageImpl<>(
        pageOfOrders.getContent().stream().map(orderMapper::OrderToOrderResponse).collect(Collectors.toList()),
        page,
        pageOfOrders.getTotalElements()
    );
    return new DataResponse(pageOfOrderResponse);
  }

  @Override
  @Transactional
  public DataResponse insertOrder(AddOrderRequest request) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    ShopOrder order = orderMapper.AddOrderRequestToOrder(request);
    Set<OrderDetail> setOfOrderDetails = order.getSetOfOrderDetails();
    List<Product> listOfProducts = new ArrayList<>();
    for (OrderDetail orderDetail : setOfOrderDetails) {
      Product product = orderDetail.getProduct();
      if (product.getTotal() >= orderDetail.getQuantity()) {
        product.setTotal(product.getTotal() - orderDetail.getQuantity());
        product.setSelAmount(product.getSelAmount() + orderDetail.getQuantity());
        listOfProducts.add(product);
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_QUANTITY_NOT_ENOUGH,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    productRepository.saveAllAndFlush(listOfProducts);

    Cart cart = cartRepository.findByUser_Id(userId).get();
    Set<CartItem> setOfCartItems = cart.getSetOfCartItems();
    setOfCartItems = setOfCartItems.stream().filter(cartItem -> {
      for (Product product : listOfProducts) {
        if (cartItem.getProduct().getId().equals(product.getId())) {
          return true;
        }
      }
      return false;
    }).collect(Collectors.toSet());
    cart.getSetOfCartItems().removeAll(setOfCartItems);
    cartItemRepository.deleteAll(setOfCartItems);

    if (order.getPaymentMethod().getId() == PaymentMethodEnum.COD.getId()) {
      order.setOrderStatus(orderStatusRepository.findById(OrderStatusEnum.PROCCESSING.getId()).get());
    } else {
      order.setOrderStatus(orderStatusRepository.findById(OrderStatusEnum.AWAITINGPAYMENT.getId()).get());
      order = orderRepository.save(order);
      orderDetailRepository.saveAll(order.getSetOfOrderDetails());
      MomoPaymentResponse momoResponse = paymentService.execute(order);
      return new DataResponse(momoResponse);
    }
    order = orderRepository.save(order);
    orderDetailRepository.saveAll(order.getSetOfOrderDetails());

    return new DataResponse(ApplicationConstants.ORDER_SUCCESSFULLY, orderMapper.OrderToOrderResponse(order));
  }

  @Override
  public DataResponse changeOrderStatus(ChangeOrderStatusRequest request) {
    long orderId = request.getOrderId();
    long statusId = request.getStatusId();
    System.out.println("test: " + orderId);
    Optional<ShopOrder> optionalShopOrder = orderRepository.findById(orderId);
    if (optionalShopOrder.isPresent()) {
      ShopOrder order = optionalShopOrder.get();
      Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(statusId);
      if (optionalOrderStatus.isPresent()) {
        order.setOrderStatus(optionalOrderStatus.get());
        order = orderRepository.saveAndFlush(order);
        return new DataResponse(orderMapper.OrderToOrderResponse(order));
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_STATUS_NOT_FOUND,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse cancelOrder(long orderId) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    Optional<ShopOrder> optionalShopOrder = orderRepository.findByIdAndUser_IdAndDeletedAtIsNull(orderId, userId);
    if (optionalShopOrder.isPresent()) {
      ShopOrder order = optionalShopOrder.get();
      if (order.getOrderStatus().getId().equals(OrderStatusEnum.AWAITINGPAYMENT.getId()) || (order.getOrderStatus()
          .getId().equals(OrderStatusEnum.PROCCESSING.getId()))) {
        order.setOrderStatus(orderStatusRepository.findById(OrderStatusEnum.CANCELED.getId()).get());
        order = orderRepository.saveAndFlush(order);
        return new DataResponse(ApplicationConstants.ORDER_CANCEL_SUCCESSFULLY,
            orderMapper.OrderToOrderResponse(order));
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST,
            ApplicationConstants.ONLY_AWAITING_PAYMENT_ORDER_CAN_BE_CANCELED, ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse updateMomoPaymentOrder(MomoPaymentResultDto momoPaymentResultDto) {
//    MomoPaymentResultDto result = new MomoPaymentResultDto(request);
//    response.setHeader("Location", "https://td-shop.vercel.app/?t1=1&t2=2");
//    response.setStatus(302);
    MomoPaymentResultDto result = momoPaymentResultDto;
    if (result.checkIfSignatureValid(momoConfig)) {
      String data = new String(Base64.getDecoder().decode(result.getExtraData().getBytes(StandardCharsets.UTF_8)));
      Map<String, String> map = gson.fromJson(data, Map.class);
      Long orderId = Long.valueOf(map.get("orderId"));
      Optional<ShopOrder> optionalOrder = orderRepository.findById(orderId);
      if (optionalOrder.isPresent()) {
        ShopOrder order = optionalOrder.get();
        OrderStatus orderStatus = orderStatusRepository.findById(OrderStatusEnum.PROCCESSING.getId()).get();
        order.setOrderStatus(orderStatus);
        orderRepository.saveAndFlush(order);

        sendMessage(order.getUser().getId());
        return DataResponse.SUCCESSFUL;
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PAYMENT_SIGNATURE_INCORRECT,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public SseEmitter registerClient(Long userId) {
//    long userId = 4;
    SseEmitter emitter = new SseEmitter((long) 5 * 1000 * 60);
    emitter.onCompletion(() -> clients.remove(userId));
    emitter.onError((err) -> removeAndLogError(userId));
    emitter.onTimeout(() -> removeAndLogError(userId));
    clients.put(userId, emitter);

    log.info("New client registered {}", userId);
    return emitter;
  }

  private void removeAndLogError(Long userId) {
    log.info("Error during communication. Unregister client {}", userId);
    clients.remove(userId);
  }

  public void sendMessage(Long userId) {
    SseEmitter sseEmitter = clients.get(userId);
    try {
      log.info("Notify client {}", userId);
      SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event().name("payment_result")
          .data(new DataResponse(Boolean.valueOf(true)), MediaType.APPLICATION_JSON);
      sseEmitter.send(eventBuilder);
    } catch (IOException e) {
      log.error(e.getMessage());
      sseEmitter.completeWithError(e);
    }
  }

}
