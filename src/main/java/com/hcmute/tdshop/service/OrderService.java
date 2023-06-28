package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.order.AddOrderRequest;
import com.hcmute.tdshop.dto.order.ChangeOrderStatusRequest;
import com.hcmute.tdshop.dto.payment.momo.MomoPaymentResultDto;
import com.hcmute.tdshop.model.DataResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface OrderService {

  public DataResponse getOrder(Pageable page);
  public DataResponse searchOrder(Long orderId, Long statusId, Pageable page);

  public DataResponse getUserOrder(Long orderId, Long status, Pageable page);

  public DataResponse insertOrder(AddOrderRequest request);

  public DataResponse changeOrderStatus(ChangeOrderStatusRequest request);

  public DataResponse cancelOrder(long orderId);

  public DataResponse updateMomoPaymentOrder(MomoPaymentResultDto momoPaymentResultDto);

  public SseEmitter registerClient(Long userId);
  public void sendMessage(Long userId, int result);
  public void sendDummyMessage(Long userId);

  public DataResponse rePayment(long orderId);

  public DataResponse adminCancelOrder(long orderId);
}
