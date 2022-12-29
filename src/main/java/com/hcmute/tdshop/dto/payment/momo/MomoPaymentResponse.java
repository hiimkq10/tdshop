package com.hcmute.tdshop.dto.payment.momo;

import lombok.Data;

@Data
public class MomoPaymentResponse {
  String partnerCode;
  String requestId;
  String orderId;
  String amount;
  String responseTime;
  String message;
  String resultCode;
  String payUrl;
  String deeplinkp;
  String qrCodeUrl;
}
