package com.hcmute.tdshop.dto.payment.momo;

import com.mservice.shared.utils.Encoder;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomoPaymentResultDto {

  private String partnerCode;
  private String orderId;
  private String requestId;
  private Long amount;
  private String orderInfo;
  private String orderType;
  private Long transId;
  private Integer resultCode;
  private String message;
  private String payType;
  private Long responseTime;
  private String extraData;
  private String signature;

  public MomoPaymentResultDto(HttpServletRequest request) {
    this.partnerCode = request.getParameter("partnerCode");
    this.orderId = request.getParameter("orderId");
    this.requestId = request.getParameter("requestId");
    this.amount = Long.valueOf(request.getParameter("amount"));
    this.orderInfo = request.getParameter("orderInfo");
    this.orderType = request.getParameter("orderType");
    this.transId = Long.valueOf(request.getParameter("transId"));
    this.resultCode = Integer.valueOf(request.getParameter("resultCode"));
    this.message = request.getParameter("message");
    this.payType = request.getParameter("payType");
    this.responseTime = Long.valueOf(request.getParameter("responseTime"));
    this.extraData = request.getParameter("extraData");
    this.signature = request.getParameter("signature");
  }

  public boolean checkIfSignatureValid(MomoConfig momoConfig) {
    try {
      String rawSignature =
          "accessKey=" + momoConfig.getAccessKey() + "&amount=" + amount + "&extraData=" + extraData + "&message="
              + message + "&orderId="
              + orderId + "&orderInfo=" + orderInfo + "&orderType=" + orderType + "&partnerCode=" + partnerCode
              + "&payType=" + payType
              + "&requestId=" + requestId + "&responseTime=" + responseTime + "&resultCode=" + resultCode + "&transId="
              + transId;
      String signRequest = Encoder.signHmacSHA256(rawSignature, momoConfig.getSecretKey());
      return signRequest.equals(signature);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    return false;
  }
}
