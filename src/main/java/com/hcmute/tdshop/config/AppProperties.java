package com.hcmute.tdshop.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConfigurationProperties()
@Component
public class AppProperties {
  @Value("${app.authorizedRedirectUris}")
  private List<String> authorizedRedirectUris = new ArrayList<>();

  @Value("${fe.base.url}")
  private String feBaseUrl;

  @Value("${order.payment.time-out}")
  private Long orderPaymentTimeOut;

  @Value("${order.awaiting-payment.time-out}")
  private Long orderAwaitingPaymentTimeOut;

  public List<String> getAuthorizedRedirectUris() {
    return authorizedRedirectUris;
  }

  public String getFeBaseUrl() {
    return feBaseUrl;
  }

  public Long getOrderPaymentTimeOut() {
    return orderPaymentTimeOut;
  }

  public Long getOrderAwaitingPaymentTimeOut() {
    return orderAwaitingPaymentTimeOut;
  }
}
