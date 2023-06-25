package com.hcmute.tdshop.service.payment;

import com.google.gson.Gson;
import com.hcmute.tdshop.dto.payment.momo.MomoConfig;
import com.hcmute.tdshop.dto.payment.momo.MomoPaymentRequest;
import com.hcmute.tdshop.dto.payment.momo.MomoPaymentResponse;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.ShopOrder;
import com.mservice.shared.exception.MoMoException;
import com.mservice.shared.sharedmodels.HttpResponse;
import com.mservice.shared.utils.Execute;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl {

  @Autowired
  MomoConfig momoConfig;

  private final Gson gson = new Gson();

  public MomoPaymentResponse execute(ShopOrder order) {
    try {
      MomoPaymentRequest momoPaymentDto = ShopOrderToMoMoPaymentDto(order);
      String payload = gson.toJson(momoPaymentDto, MomoPaymentRequest.class);
      Execute execute = new Execute();
      HttpResponse response = execute.sendToMoMo(momoPaymentDto.getEndpoint(), payload);

      System.out.println(response);
      if (response.getStatus() != 200) {
        throw new MoMoException("[PaymentResponse] [" + momoPaymentDto.getOrderId() + "] -> Error API");
      }

      MomoPaymentResponse captureMoMoResponse = gson.fromJson(response.getData(), MomoPaymentResponse.class);
      return captureMoMoResponse;

    } catch (Exception exception) {
      log.error("[PaymentMoMoResponse] " + exception);
      throw new IllegalArgumentException("Invalid params capture MoMo Request");
    }
  }

  public MomoPaymentRequest ShopOrderToMoMoPaymentDto(ShopOrder order) {
    double total = order.getShipPrice();
    for (OrderDetail orderDetail : order.getSetOfOrderDetails()) {
      total += (orderDetail.getFinalPrice() * orderDetail.getQuantity());
    }
    total = total / 1000;
    Map<String, String> map = new HashMap<>();
    map.put("orderId", String.valueOf(order.getId()));
    String extraData = Base64.getEncoder().encodeToString(gson.toJson(map).getBytes(StandardCharsets.UTF_8));
    return new MomoPaymentRequest(momoConfig, String.valueOf(UUID.randomUUID()), "ORDER", momoConfig.getRedirectUrl(),
        momoConfig.getIpnUrl(), String.valueOf((long) total), extraData);
  }
}
