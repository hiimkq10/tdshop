package com.hcmute.tdshop.dto.payment.momo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class MomoConfig {

  @Value("${MOMO_PARTNER_CODE}")
  String partnerCode;

  @Value("${MOMO_ACCESS_KEY}")
  String accessKey;

  @Value("${MOMO_SECRET_KEY}")
  String secretKey;

  @Value("${MOMO_API_ENDPOINT}")
  String endpoint;

  @Value("${MOMO_REDIRECT_URL}")
  String redirectUrl;

  @Value(("${MOMO_REDIRECT_URL}"))
  String ipnUrl;

  public String getPartnerCode() {
    return partnerCode;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public String getIpnUrl() {return ipnUrl;}
}
