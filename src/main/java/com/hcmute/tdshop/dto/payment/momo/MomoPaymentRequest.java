package com.hcmute.tdshop.dto.payment.momo;

import com.mservice.shared.utils.Encoder;
import java.util.UUID;
import lombok.Data;

@Data
public class MomoPaymentRequest {

  String partnerCode;
  String accessKey;
  String secretKey;
  String endpoint;
  String orderInfo;
  String redirectUrl;
  String ipnUrl;
  String amount;
  String orderId;
  String requestId = String.valueOf((UUID.randomUUID()));
  String requestType = "captureWallet";
  String extraData;
  String signature;

  public MomoPaymentRequest(MomoConfig momoConfig, String orderId, String orderInfo, String redirectUrl, String ipnUrl,
      String amount, String extraData) {
    this.partnerCode = momoConfig.getPartnerCode();
    this.accessKey = momoConfig.getAccessKey();
    this.secretKey = momoConfig.getSecretKey();
    this.endpoint = momoConfig.getEndpoint();
    this.orderId = orderId;
    this.orderInfo = orderInfo;
    this.redirectUrl = redirectUrl;
    this.ipnUrl = ipnUrl;
    this.amount = amount;
    this.extraData = extraData;
    this.signature = this.createSignature();
  }

  public String createSignature() {
    String signRequest = "";
    try {
      String rawSignature =
          "accessKey=" + accessKey + "&amount=" + amount + "&extraData=" + extraData + "&ipnUrl=" + ipnUrl + "&orderId="
              + orderId + "&orderInfo=" + orderInfo + "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl
              + "&requestId=" + requestId + "&requestType=" + requestType;
      System.out.println(rawSignature);
      signRequest = Encoder.signHmacSHA256(rawSignature, secretKey);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    return signRequest;
  }
}
