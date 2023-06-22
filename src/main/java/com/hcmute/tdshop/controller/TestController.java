package com.hcmute.tdshop.controller;

import com.google.gson.Gson;
import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.entity.Notification;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.NotificationRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.service.ReviewService;
import com.hcmute.tdshop.service.shipservices.LalamoveShipService;
import com.hcmute.tdshop.utils.ExcelUtil;
import com.hcmute.tdshop.utils.notification.NotificationHelper;
import com.twilio.rest.microvisor.v1.App;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {
  Logger logger = LoggerFactory.getLogger(TestController.class);
  @Autowired
  AppProperties appProperties;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  NotificationHelper notificationHelper;

  @Autowired
  NotificationRepository notificationRepository;

  @Autowired
  ExcelUtil excelUtil;

  @GetMapping("/")
  public DataResponse test() {
    AppProperties appProperties = new AppProperties();
    return new DataResponse(appProperties.getAuthorizedRedirectUris());
  }

  @PostMapping("/posttest")
  public void postTest() {
    appProperties.getAuthorizedRedirectUris().forEach(item -> System.out.println(item));
//    authorizedRedirectUris.forEach(item -> System.out.println(item));
    System.out.println("Test");
//    reviewService.getAll(Pageable.ofSize(10));
//    return new DataResponse(appProperties.getOauth2().getAuthorizedRedirectUris());
  }

  @GetMapping("/test-new-pro-noti/{id}")
  public DataResponse testNewProNoti(@PathVariable(name = "id") Long id) {
    Product product = productRepository.findById(id).get();
    Notification notification = notificationHelper.buildNewProductAddedNotification(product);
    notification = notificationRepository.save(notification);
    return new DataResponse(notification);
  }

  @GetMapping("/test-pro-out-noti/{id}")
  public DataResponse testProOutNoti(@PathVariable(name = "id") Long id) {
    Product product = productRepository.findById(id).get();
    Notification notification = notificationHelper.buildProductOutOfStockNotification(product);
    notification = notificationRepository.save(notification);
    return new DataResponse(notification);
  }

  @GetMapping("/test-excel")
  public DataResponse testExcel()
      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    excelUtil.insertDataToDatabase();
    return DataResponse.SUCCESSFUL;
  }

  @GetMapping("/test-lalamove-auth")
  public DataResponse testLalamoveAuth() throws Exception {
    Gson gsonObj = new Gson();
    long time = 1685698441197L;
    String apiKey = "pk_test_d7927da311af469288019b4d58759591";
    String apiSecret = "sk_test_hthbEAkPLAJASotSyzblBSlFegxhN7d8pNELRASkccZ2vKHk0Ddc2pnIi9FkHZGn";

    Map<String, Object> bodyData = new HashMap<>();
    Map<String, String> data = new HashMap<>();
    data.put("serviceType", "MOTORCYCLE");
    bodyData.put("data", data);
    String json = gsonObj.toJson(bodyData);
    logger.info(json);
    String signatureData = String.format("%d%nPOST%n/v3/quotations%n%n%s", time, "Hello world");
    logger.info(signatureData);
    String signature = myEncode(apiSecret, "1685698441197POST/v3/quotationsHello world");
    return new DataResponse(signature);
  }

  public static String myEncode(String key, String data) throws Exception {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    sha256_HMAC.init(secret_key);

    return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
  }
}
