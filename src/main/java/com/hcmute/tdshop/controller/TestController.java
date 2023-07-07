package com.hcmute.tdshop.controller;

import com.google.gson.Gson;
import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.entity.Notification;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.enums.ProductUserInteractExcel;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.NotificationRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.service.ReviewService;
import com.hcmute.tdshop.service.shipservices.LalamoveShipService;
import com.hcmute.tdshop.utils.ExcelUtil;
import com.hcmute.tdshop.utils.exporter.ProductUserInteractExcelExporter;
import com.hcmute.tdshop.utils.notification.NotificationHelper;
import com.twilio.rest.microvisor.v1.App;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
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

//  @GetMapping("/test-export-excel-data")
//  public DataResponse testExcel(HttpServletResponse response)
//      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
////    response.setContentType("application/octet-stream");
////    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
////    String currentDateTime = dateFormatter.format(new Date());
////
////    String headerKey = "Content-Disposition";
////    String headerValue = "attachment; filename=aidata_" + currentDateTime + ".xlsx";
////    response.setHeader(headerKey, headerValue);
//    List<ProductUserInteractExcel> data = new ArrayList<>();
//    data.add(new ProductUserInteractExcel(1L, 1L, 1, 5));
//    data.add(new ProductUserInteractExcel(2L, 2L, 0, 4));
//    data.add(new ProductUserInteractExcel(1L, 1L, 1, 5));
//    data.add(new ProductUserInteractExcel(2L, 2L, 0, 4));
//    data.add(new ProductUserInteractExcel(1L, 1L, 1, 5));
//    data.add(new ProductUserInteractExcel(2L, 2L, 0, 4));
//    ProductUserInteractExcelExporter exporter = new ProductUserInteractExcelExporter(data);
//    exporter.export(response);
//    return DataResponse.SUCCESSFUL;
//  }

  @GetMapping("/test-gen-ai-data")
  public DataResponse testUserExcel()
      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    List<ProductUserInteractExcel> data = excelUtil.generateDataForRecomendationSystem();
    ProductUserInteractExcelExporter exporter;
    int size = data.size();
    int min = 0;
    int max = 0;
    int i = 1;
    while (max != size) {
      min = max;
      max += 2000;
      if (max > size) {
        max = size;
      }
      exporter = new ProductUserInteractExcelExporter(data.subList(min, max));
      exporter.export("src/main/resources/data/aidata" + i + ".xlsx");
      i++;
    }
//    ProductUserInteractExcelExporter exporter = new ProductUserInteractExcelExporter(data);
//    exporter.export("src/main/resources/data/aidata.xlsx");
    return DataResponse.SUCCESSFUL;
  }

//  @GetMapping("/test-user-excel")
//  public DataResponse testUserExcel()
//      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//    excelUtil.insertUsersToDatabase();
//    return DataResponse.SUCCESSFUL;
//  }
//
//  @GetMapping("/test-product-excel")
//  public DataResponse testProductExcel()
//      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//    excelUtil.insertProductToDatabase();
//    return DataResponse.SUCCESSFUL;
//  }

//  @GetMapping("/test-excel")
//  public DataResponse testExcel()
//      throws IOException, ParseException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//    excelUtil.insertDataToDatabase();
//    return DataResponse.SUCCESSFUL;
//  }

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
//    String signatureData = String.format("%d%nPOST%n/v3/quotations%n%n%s", time, json);
    String signatureData = String.format("%d\r\nPOST\r\n/v3/quotations\r\n\r\n%s", time, json);
    logger.info(signatureData);
    String signature = myEncode(apiSecret, signatureData);
    return new DataResponse(signature);
  }

  public static String myEncode(String key, String data) throws Exception {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    sha256_HMAC.init(secret_key);

    return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
  }
}
