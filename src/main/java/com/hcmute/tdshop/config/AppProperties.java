package com.hcmute.tdshop.config;

import com.auth0.jwt.algorithms.Algorithm;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@ConfigurationProperties()
@Component
@Getter
public class AppProperties {

  @Value("${app.authorizedRedirectUris}")
  private List<String> authorizedRedirectUris = new ArrayList<>();

  @Value("${fe.base.url}")
  private String feBaseUrl;

  @Value("${order.payment.time-out}")
  private Long orderPaymentTimeOut;

  @Value("${order.awaiting-payment.time-out}")
  private Long orderAwaitingPaymentTimeOut;

  @Value("${spring.mail.username}")
  private String sender;

  @Value("${app.notification.new-product}")
  private String newProductAddedNotificationTypeId;

  @Value("${app.notification.product-out-of-stock}")
  private String productOutOfStockNotificationTypeId;

  @Value("${app.email.confirm-token.duration}")
  private int confirmTokenDuration = 5;

  @Value("${app.email.reset-password.duration}")
  private int resetPasswordDuration = 5;

  @Value("${app.jwt-token.secret}")
  private String jwtSecret;

  @Value("${app.jwt-token.access-token-expiration}")
  private Long jwtAccessTokenExpiration;

  @Value("${app.jwt-token.refresh-token-expiration}")
  private Long jwtRefreshTokenExpiration;

  @Value("${app.recommend-ai.base-url}")
  private String recommendAIBaseUrl;
}
