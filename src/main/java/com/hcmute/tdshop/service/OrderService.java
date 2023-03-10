package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.order.AddOrderRequest;
import com.hcmute.tdshop.dto.order.ChangeOrderStatusRequest;
import com.hcmute.tdshop.model.DataResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  public DataResponse getOrder(Pageable page);
  public DataResponse searchOrder(Long orderId, Long statusId, Pageable page);

  public DataResponse getUserOrder(Long orderId, Long status, Pageable page);

  public DataResponse insertOrder(AddOrderRequest request);

  public DataResponse changeOrderStatus(ChangeOrderStatusRequest request);

  public DataResponse cancelOrder(long orderId);

  public DataResponse updateMomoPaymentOrder(HttpServletRequest request, HttpServletResponse response);
}
