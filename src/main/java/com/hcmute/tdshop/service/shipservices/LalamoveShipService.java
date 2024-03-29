package com.hcmute.tdshop.service.shipservices;

import com.google.gson.Gson;
import com.hcmute.tdshop.dto.ResultDto;
import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.dto.shipservices.CalculateDeliveryTimeRequest;
import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.CancelOrderRequest;
import com.hcmute.tdshop.dto.shipservices.CheckShipConditionDto;
import com.hcmute.tdshop.dto.shipservices.Coordinate;
import com.hcmute.tdshop.dto.shipservices.CreateOrderRequest;
import com.hcmute.tdshop.dto.shipservices.DeliveryTimeDto;
import com.hcmute.tdshop.dto.shipservices.OrderSize;
import com.hcmute.tdshop.dto.shipservices.ProductParameters;
import com.hcmute.tdshop.dto.shipservices.ShipOrderDto;
import com.hcmute.tdshop.dto.shipservices.lalamove.CancelOrderResponse;
import com.hcmute.tdshop.dto.shipservices.lalamove.CreateOrderDto;
import com.hcmute.tdshop.dto.shipservices.lalamove.CreateOrderResponse;
import com.hcmute.tdshop.dto.shipservices.lalamove.GetOrderData;
import com.hcmute.tdshop.dto.shipservices.lalamove.GetOrderResponse;
import com.hcmute.tdshop.dto.shipservices.lalamove.GetQuotationResponse;
import com.hcmute.tdshop.dto.shipservices.lalamove.Item;
import com.hcmute.tdshop.dto.shipservices.lalamove.QuotationDto;
import com.hcmute.tdshop.dto.shipservices.lalamove.Recipent;
import com.hcmute.tdshop.dto.shipservices.lalamove.Sender;
import com.hcmute.tdshop.dto.shipservices.lalamove.Stop;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.OrderStatus;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShipData;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.enums.LalamoveServiceEnum;
import com.hcmute.tdshop.enums.LalamoveShipStatusEnum;
import com.hcmute.tdshop.enums.LalamoveWeightEnum;
import com.hcmute.tdshop.enums.OrderStatusEnum;
import com.hcmute.tdshop.enums.PaymentMethodEnum;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.OrderStatusRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;

@Service("LalamoveShipService")
public class LalamoveShipService extends ShipServices {

  Logger logger = LoggerFactory.getLogger(LalamoveShipService.class);

  @Autowired
  private OrderStatusRepository orderStatusRepository;

  @Autowired
  private ShopOrderRepository shopOrderRepository;

  @Value("${lalamove.api.key}")
  String key;

  @Value("${lalamove.api.secret}")
  String secret;

  @Value("${lalamove.base.url}")
  String baseUrl;

  @Value("${lalamove.market}")
  String market;

  @Value("${lalamove.language}")
  String language;

  @Value("${lalamove.order.cancel.allowed-status}")
  List<String> cancelAllowedStatus;

  @Value("${lalamove.order.create.support-regions}")
  List<Long> supportRegions;

  List<String> allowCancelShopOrderStatus = Arrays.asList(
      LalamoveShipStatusEnum.LALAMOVE_NOT_CREATED.getCode(),
      LalamoveShipStatusEnum.LALAMOVE_CANCELED.getCode(),
      LalamoveShipStatusEnum.LALAMOVE_EXPIRED.getCode()
  );

  @Override
  public boolean checkSize(ShopOrder order) {
    return true;
  }

  @Override
  public boolean checkProductSize(OrderSize orderSize) {
    return true;
  }

  @Override
  public boolean checkAllowCancelOrder(String statusCode) {
    if (cancelAllowedStatus.contains(statusCode)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean checkCODAmount(ShopOrder order) {
    return true;
  }

  @Override
  public boolean checkRegion(Address address) {
    if (supportRegions.contains(address.getWards().getDistrict().getProvince().getId())) {
      return true;
    }
    return false;
  }

  @Override
  public ShipOrderDto getShipOrder(ShopOrder order) {
    GetOrderData getOrderData = getOrder(order);
    return shipServicesMapper.LalamoveGetOrderDataToShipOrderDto(getOrderData);
  }

  public String getService(Address address, ProductParameters parameters) {
    final double mWeight = 30;
    final double mLength = 500;
    final double mWidth = 400;
    final double mHeight = 500;
    if (address.getWards().getDistrict().getProvince().getId() == 31) {
      return LalamoveServiceEnum.TRUCK175.name();
    }
    if (parameters.getWeight() > mWeight || parameters.getLength() > mLength || parameters.getWidth() > mWidth
        || parameters.getHeight() > mHeight) {
      return LalamoveServiceEnum.TRUCK175.name();
    }
    return LalamoveServiceEnum.MOTORCYCLE.name();
  }

  public GetOrderData getOrder(ShopOrder order) {
    try {
      Optional<ShipData> optionalData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null)
          .findFirst();
      if (!optionalData.isPresent()) {
        return null;
      }
      ShipData shipData = optionalData.get();

      Date date = new Date();
      long time = date.getTime();
      String signatureData = String.format("%d\r\nGET\r\n/v3/orders/%s\r\n\r\n", time, shipData.getShipOrderId());
      String signature = encode(secret, signatureData);

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Authorization", String.format("hmac %s:%d:%s", key, time, signature))
          .defaultHeader("Market", market).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/v3/orders/{id}").build(shipData.getShipOrderId()));

      Mono<GetOrderResponse> response = bodySpec.retrieve().bodyToMono(GetOrderResponse.class);
      GetOrderResponse getOrderResponse = response.block();
      if (getOrderResponse == null || getOrderResponse.getData() == null) {
        return null;
      }
      return getOrderResponse.getData();
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  @Override
  public DataResponse createOrder(CreateOrderRequest dto) {
    Optional<ShopOrder> optionalData = shopOrderRepository.findById(dto.getOrderId());
    if (!optionalData.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND,
          ApplicationConstants.ORDER_NOT_FOUND_CODE);
    }
    ShopOrder order = optionalData.get();
    OrderSize orderSize = new OrderSize(dto.getLength(), dto.getWidth(), dto.getHeight(), dto.getWeight());

    CheckShipConditionDto checkShipConditionDto = checkShipCondition(order, true);
    if (!checkShipConditionDto.isResult()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, checkShipConditionDto.getMessage(),
          checkShipConditionDto.getMessageCode());
    }

    try {
      GetOrderData getOrderData = getOrder(order);
      if (!(getOrderData == null || getOrderData.getStatus().equals(
          LalamoveShipStatusEnum.LALAMOVE_CANCELED.getCode()) || getOrderData.getStatus().equals(
          LalamoveShipStatusEnum.LALAMOVE_EXPIRED.getCode()))) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_EXISTED,
            ApplicationConstants.SHIP_DATA_ORDER_EXISTED_CODE);
      }

      CalculateFeeDto calculateFeeDto = new CalculateFeeDto();
      calculateFeeDto.setShipId(order.getShip().getId());
      calculateFeeDto.setAddressId(order.getAddress().getId());
      calculateFeeDto.setSetOfProducts(
          order.getSetOfOrderDetails().stream().map(o -> new OrderProductDto(o.getProduct().getId(), o.getQuantity()))
              .collect(
                  Collectors.toSet()));
      QuotationDto quotationDto = getQuotation(calculateFeeDto, orderSize);

      Gson gsonObj = new Gson();
      Address address = order.getAddress();

      Sender sender = new Sender();
      sender.setStopId(quotationDto.getStops().get(0).getStopId());
      sender.setName(shopName);
      sender.setPhone(String.format("+84%s", shopPhone.substring(1)));

      List<Recipent> recipents = new ArrayList<>();
      Recipent recipent = new Recipent();
      recipent.setStopId(quotationDto.getStops().get(1).getStopId());
      recipent.setName(address.getName());
      recipent.setPhone(String.format("+84%s", address.getPhone().substring(1)));
      recipents.add(recipent);

      Map<String, Object> bodyData = new HashMap<>();
      Map<String, Object> data = new HashMap<>();
      data.put("quotationId", quotationDto.getQuotationId());
      data.put("sender", sender);
      data.put("recipients", recipents);
      data.put("isRecipientSMSEnabled", true);
      data.put("isPODEnabled", false);
      bodyData.put("data", data);

      Date date = new Date();
      long time = date.getTime();
      String json = gsonObj.toJson(bodyData);
      String signatureData = String.format("%d\r\nPOST\r\n/v3/orders\r\n\r\n%s", time, json);
      String signature = encode(secret, signatureData);

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Authorization", String.format("hmac %s:%d:%s", key, time, signature))
          .defaultHeader("Market", market).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.post();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/v3/orders").build());

      bodySpec.body(BodyInserters.fromValue(json));

      Mono<CreateOrderResponse> response = bodySpec.retrieve().bodyToMono(CreateOrderResponse.class);
      CreateOrderResponse createOrderResponse = response.block();
      if (createOrderResponse == null || createOrderResponse.getData() == null) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }

      CreateOrderDto createOrderDto = createOrderResponse.getData();

      // Save Ship Data
      List<ShipData> shipDatas = shipDataRepository.findByOrder_IdAndDeletedAtIsNull(order.getId());
      LocalDateTime now = LocalDateTime.now();
      int size = shipDatas.size();
      for (int i = 0; i < size; i++) {
        shipDatas.get(i).setDeletedAt(now);
      }
      shipDatas.add(new ShipData(null, createOrderDto.getOrderId(), now, null, order));
      shipDataRepository.saveAllAndFlush(shipDatas);
      order.setShipData(shipDatas);

      OrderStatus deliveringStatus = orderStatusRepository.findById(OrderStatusEnum.DELIVERING.getId()).orElse(null);
      if (deliveringStatus == null) {
        logger.error("Order status config is incorrect");
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      order.setOrderStatus(deliveringStatus);
      shopOrderRepository.saveAndFlush(order);

      ShipOrderDto shipOrderDto = getShipOrder(order);
      return new DataResponse(orderMapper.OrderToOrderWithShipDataResponse(order, shipOrderDto));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  @Override
  public DataResponse cancelOrder(CancelOrderRequest dto) {
    Optional<ShopOrder> optionalOrderData = shopOrderRepository.findById(dto.getOrderId());
    if (!optionalOrderData.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_NOT_FOUND,
          ApplicationConstants.ORDER_NOT_FOUND_CODE);
    }
    ShopOrder order = optionalOrderData.get();
    ResultDto result = checkCancelShipOrderCondition(order);
    if (!result.isResult()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_STATUS_NOT_DILIVERING, order,
          ApplicationConstants.ORDER_STATUS_NOT_DILIVERING_CODE);
    }
    try {
      GetOrderData getOrderData = getOrder(order);
      if (getOrderData == null) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
            ApplicationConstants.BAD_REQUEST_CODE);
      }

      if (!cancelAllowedStatus.contains(getOrderData.getStatus())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW,
            ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW_CODE);
      }

      Date date = new Date();
      long time = date.getTime();
      String signatureData = String.format("%d\r\nDELETE\r\n/v3/orders/%s\r\n\r\n", time, getOrderData.getOrderId());
      String signature = encode(secret, signatureData);

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Authorization", String.format("hmac %s:%d:%s", key, time, signature))
          .defaultHeader("Market", market).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.delete();
      RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> uriBuilder.path("/v3/orders/{orderId}")
          .build(getOrderData.getOrderId()));
      bodySpec.retrieve().bodyToMono(CancelOrderResponse.class).block();

      OrderStatus proccessingStatus = orderStatusRepository.findById(OrderStatusEnum.PROCCESSING.getId()).orElse(null);
      if (proccessingStatus == null) {
        logger.error("Order status config is incorrect");
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      order.setOrderStatus(proccessingStatus);
      shopOrderRepository.saveAndFlush(order);

      ShipOrderDto shipOrderDto = getShipOrder(order);
      return new DataResponse(orderMapper.OrderToOrderWithShipDataResponse(order, shipOrderDto));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  @Override
  public DataResponse calculateExpectedDeliveryTime(CalculateDeliveryTimeRequest dto) {
    Optional<Address> optionalData = addressRepository.findById(dto.getAddressId());
    if (!optionalData.isPresent()) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    Address address = optionalData.get();
    Long provinceId = address.getWards().getDistrict().getProvince().getId();
    LocalDateTime time = LocalDateTime.now();
    if (provinceId == 79) {
      time = time.plusDays(3);
    } else if (provinceId == 31) {
      time = time.plusDays(5);
    } else {
      time = time.plusDays(7);
    }
    return new DataResponse(new DeliveryTimeDto(time));
  }

  @Override
  public CheckShipConditionDto checkShipCondition(ShopOrder order, boolean statusCheck) {
    if (statusCheck && order.getOrderStatus().getId() != OrderStatusEnum.PROCCESSING.getId()) {
      return new CheckShipConditionDto(false, ApplicationConstants.ORDER_STATUS_NOT_PROCESSING,
          ApplicationConstants.ORDER_STATUS_NOT_PROCESSING_CODE);
    }
    if (order.getPaymentMethod().getId() == PaymentMethodEnum.COD.getId()) {
      return new CheckShipConditionDto(false, ApplicationConstants.ORDER_LALAMOVE_COD_NOT_SUPPORT,
          ApplicationConstants.ORDER_LALAMOVE_COD_NOT_SUPPORT_CODE);
    }
    if (!checkRegion(order.getAddress())) {
      return new CheckShipConditionDto(false, ApplicationConstants.ORDER_LALAMOVE_REGION_NOT_SUPPORT,
          ApplicationConstants.ORDER_LALAMOVE_REGION_NOT_SUPPORT_CODE);
    }
    return new CheckShipConditionDto(true, ApplicationConstants.SUCCESSFUL, ApplicationConstants.SUCCESSFUL_CODE);
  }

  @Override
  public ResultDto checkAllowCancelShopOrder(ShopOrder order) {
    ShipOrderDto shipOrderDto = getShipOrder(order);
    if (allowCancelShopOrderStatus.contains(shipOrderDto.getStatusCode())) {
      return ResultDto.SUCCESS;
    }
    return new ResultDto(false, ApplicationConstants.ORDER_SHIP_NOT_CANCELED,
        ApplicationConstants.ORDER_SHIP_NOT_CANCELED_CODE);
  }

  @Override
  public OrderStatusEnum getShopOrderStatus(ShopOrder order) {
    ShipOrderDto shipOrderDto = getShipOrder(order);
    List<String> proccessingStatuses = Arrays.asList(
        LalamoveShipStatusEnum.LALAMOVE_NOT_CREATED.getCode(),
        LalamoveShipStatusEnum.LALAMOVE_CANCELED.getCode(),
        LalamoveShipStatusEnum.LALAMOVE_EXPIRED.getCode()
    );
    List<String> diliveredStatuses = Arrays.asList(
        LalamoveShipStatusEnum.LALAMOVE_COMPLETED.getCode()
    );
    if (proccessingStatuses.contains(shipOrderDto.getStatusCode())) {
      return OrderStatusEnum.PROCCESSING;
    }
    if (diliveredStatuses.contains(shipOrderDto.getStatusCode())) {
      return OrderStatusEnum.DELIVERED;
    }
    return OrderStatusEnum.DELIVERING;
  }

  @Override
  public ResultDto checkCancelShipOrderCondition(ShopOrder order) {
    if (order.getOrderStatus().getId() != OrderStatusEnum.DELIVERING.getId()) {
      return new ResultDto(false, ApplicationConstants.ORDER_STATUS_NOT_DILIVERING,
          ApplicationConstants.ORDER_STATUS_NOT_DILIVERING_CODE);
    }
    return ResultDto.SUCCESS;
  }

  @Override
  public DataResponse calculateFee(CalculateFeeDto dto) {
    QuotationDto quotationDto = getQuotation(dto, null);
    return new DataResponse(shipServicesMapper.LalamoveQuotationDtoToFeeResponse(quotationDto));
  }

  public QuotationDto getQuotation(CalculateFeeDto dto, OrderSize orderSize) {
    try {
      Gson gsonObj = new Gson();
      Optional<Address> optionalData = addressRepository.findById(dto.getAddressId());
      if (!optionalData.isPresent()) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      Address address = optionalData.get();
      Item item = generateItem(dto.getSetOfProducts());

      List<Stop> stops = new ArrayList<>();
      stops.add(
          new Stop(null, new Coordinate(shopLat, shopLng), shopAddressDetail,
              null, null));
      stops.add(generateStop(address));

      ProductParameters parameters = null;
      if (orderSize != null) {
        parameters = OrderSizeToProductParameters(orderSize);
      } else {
        parameters = calculateProductsParameters(dto.getSetOfProducts());
      }

      Map<String, Object> bodyData = new HashMap<>();
      Map<String, Object> data = new HashMap<>();
      data.put("serviceType", getService(address, parameters));
      data.put("item", item);
      data.put("language", language);
      data.put("stops", stops);
      data.put("isRouteOptimized", true);
      bodyData.put("data", data);

      Date date = new Date();
      long time = date.getTime();
      String json = gsonObj.toJson(bodyData);
      String signatureData = String.format("%d\r\nPOST\r\n/v3/quotations\r\n\r\n%s", time, json);
      String signature = encode(secret, signatureData);

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Authorization", String.format("hmac %s:%d:%s", key, time, signature))
          .defaultHeader("Market", market).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.post();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/v3/quotations").build());

      bodySpec.body(BodyInserters.fromValue(json));

      Mono<GetQuotationResponse> response = bodySpec.retrieve().bodyToMono(GetQuotationResponse.class);
      GetQuotationResponse getQuotationResponse = response.block();
      if (getQuotationResponse == null || getQuotationResponse.getData() == null) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }

      return getQuotationResponse.getData();
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  private ProductParameters OrderSizeToProductParameters(OrderSize orderSize) {
    return new ProductParameters("", 0, Math.round(orderSize.getLength()), Math.round(orderSize.getWidth()),
        Math.round(orderSize.getHeight()), Math.round(orderSize.getWeight()));
  }

  private Stop generateStop(Address address) {
    if (address == null) {
      return null;
    }
    Stop stop = new Stop();
    stop.setCoordinates(new Coordinate(String.valueOf(address.getLat()), String.valueOf(address.getLng())));
    stop.setAddress(address.getAddressDetail());
    return stop;
  }

  private Item generateItem(Set<OrderProductDto> setOfProducts) {
    List<Long> ids = setOfProducts.stream().map(OrderProductDto::getProductId).collect(Collectors.toList());
    Map<Long, Integer> productQuantityMap = new HashMap<>();
    for (OrderProductDto dto : setOfProducts) {
      productQuantityMap.put(dto.getProductId(), dto.getQuantity());
    }
    List<Product> products = productRepository.findAllById(ids);
    int quantity = 0;
    double weight = 0;
    for (Product product : products) {
      quantity += productQuantityMap.get(product.getId());
      if (weight < product.getWeight()) {
        weight = product.getWeight();
      }
    }
    Item item = new Item();
    item.setQuantity(quantity);
    if (weight < 10) {
      item.setWeight(LalamoveWeightEnum.LESS_THAN_10_KG.getCode());
    } else if (weight < 30) {
      item.setWeight(LalamoveWeightEnum.BETWEEN_10_KG_AND_30_KG.getCode());
    } else if (weight < 50) {
      item.setWeight(LalamoveWeightEnum.BETWEEN_30_KG_AND_50_KG.getCode());
    } else {
      item.setWeight(LalamoveWeightEnum.MORE_THAN_50_KG.getCode());
    }
    return item;
  }

  // Encode HMAC256
  public static String encode(String key, String data) {
    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      sha256_HMAC.init(secret_key);

      return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  public ProductParameters calculateProductsParameters(Set<OrderProductDto> setOfProducts) {
    List<Long> ids = setOfProducts.stream().map(item -> item.getProductId()).collect(Collectors.toList());
    Map<Long, Integer> productQuantityMap = new HashMap<>();
    for (OrderProductDto dto : setOfProducts) {
      productQuantityMap.put(dto.getProductId(), dto.getQuantity());
    }
    List<Product> products = productRepository.findAllById(ids);
    ProductParameters parameters = new ProductParameters();
    double length = 0.0;
    double height = 0.0;
    double width = 0.0;
    double weight = 0.0;
    for (Product product : products) {
      Integer quantity = productQuantityMap.get(product.getId());
      if (quantity == null) {
        quantity = 0;
      }
      parameters.setName(product.getName());
      parameters.setQuantity(quantity);
      if (length < product.getLength()) {
        length = product.getLength();
      }
      if (height < product.getHeight()) {
        height = product.getHeight();
      }
      width += product.getWidth();
      weight += product.getWeight() * quantity;
    }
    parameters.setLength(Math.round(length));
    parameters.setWidth(Math.round(width));
    parameters.setHeight(Math.round(height));
    parameters.setWeight(Math.round(weight));
    return parameters;
  }
}
