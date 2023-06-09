package com.hcmute.tdshop.service.shipservices;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.Coordinate;
import com.hcmute.tdshop.dto.shipservices.FeeResponse;
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
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShipData;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.mapper.ShipServicesMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AddressRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShipDataRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

@Service
public class LalamoveShipService {

  Logger logger = LoggerFactory.getLogger(LalamoveShipService.class);

  @Autowired
  ProductRepository productRepository;

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  ShipServicesMapper shipServicesMapper;

  @Autowired
  ShipDataRepository shipDataRepository;

  @Value("${td-shop.lat}")
  String shopLat;

  @Value("${td-shop.lng}")
  String shopLng;

  @Value("${td-shop.address-detail}")
  String shopAddressDetail;

  @Value("${td-shop.name}")
  String shopName;

  @Value("${td-shop.phone}")
  String shopPhone;

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

  public boolean checkRegion(Address address) {
    if (supportRegions.contains(address.getWards().getDistrict().getProvince().getId())) {
      return true;
    }
    return false;
  }

  public GetOrderData getOrder(ShopOrder order) {
    try {
      Gson gsonObj = new Gson();
      JsonObject jsonObject = new JsonObject();
      Date date = new Date();
      long time = date.getTime();
      String json = gsonObj.toJson(jsonObject);
      String signatureData = String.format("%d%nPOST%n/v3/orders%n%n%s", time, json);
      String signature = encode(secret, signatureData);

      Optional<ShipData> optionalData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null).findFirst();
      if (!optionalData.isPresent()) {
        return null;
      }
      ShipData shipData = optionalData.get();

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Authorization", String.format("hmac %s:%d:%s", key, time, signature))
          .defaultHeader("Market", market).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/v3/orders/{id}").build(shipData.getShipOrderId()));

      bodySpec.body(BodyInserters.fromValue(json));

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

  public DataResponse createOrder(ShopOrder order) {
    try {
      Optional<ShipData> optionalShipData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null).findFirst();
      if (optionalShipData.isPresent()) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_EXISTED, ApplicationConstants.SHIP_DATA_ORDER_EXISTED_CODE);
      }

      CalculateFeeDto calculateFeeDto = new CalculateFeeDto();
      calculateFeeDto.setShipId(order.getShip().getId());
      calculateFeeDto.setAddressId(order.getAddress().getId());
      calculateFeeDto.setSetOfProducts(
          order.getSetOfOrderDetails().stream().map(o -> new OrderProductDto(o.getProduct().getId(), o.getQuantity()))
              .collect(
                  Collectors.toSet()));
      QuotationDto quotationDto = getQuotation(calculateFeeDto);

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
      String signatureData = String.format("%d%nPOST%n/v3/orders%n%n%s", time, json);
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
      List<ShipData> shipDatas = shipDataRepository.findByOrder_Id(order.getId());
      LocalDateTime now = LocalDateTime.now();
      int size = shipDatas.size();
      for (int i = 0; i < size; i++) {
        if (shipDatas.get(i).getDeletedAt() == null) {
          shipDatas.get(i).setDeletedAt(now);
        }
      }
      shipDatas.add(new ShipData(null, createOrderDto.getOrderId(), now, null, order));

      return new DataResponse(true);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  public DataResponse cancelOrder(ShopOrder order) {
    try {
      GetOrderData getOrderData = getOrder(order);
      if (getOrderData == null) {
        LocalDateTime now = LocalDateTime.now();
        List<ShipData> shipDatas = shipDataRepository.findByOrder_Id(order.getId());
        int size = shipDatas.size();
        for (int i = 0; i < size; i++) {
          if (shipDatas.get(i).getDeletedAt() == null) {
            shipDatas.get(i).setDeletedAt(now);
          }
        }
        shipDataRepository.saveAllAndFlush(shipDatas);
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_NOT_FOUND, ApplicationConstants.SHIP_DATA_ORDER_NOT_FOUND_CODE);
      }

      if (!cancelAllowedStatus.contains(getOrderData.getStatus())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW, ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW_CODE);
      }

      Optional<ShipData> optionalData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null)
          .findFirst();
      if (!optionalData.isPresent()) {
        return new DataResponse(true);
      }
      ShipData shipData = optionalData.get();

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.delete();
      RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> uriBuilder.path("/v3/orders/{orderId}")
          .build(shipData.getShipOrderId()));
      bodySpec.retrieve();

      LocalDateTime now = LocalDateTime.now();
      shipData.setDeletedAt(now);
      shipDataRepository.save(shipData);

      return new DataResponse(true);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  public DataResponse calculateFee(CalculateFeeDto dto) {
    QuotationDto quotationDto = getQuotation(dto);
    return new DataResponse(shipServicesMapper.LalamoveQuotationDtoToFeeResponse(quotationDto));
  }

  public QuotationDto getQuotation(CalculateFeeDto dto) {
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

      Map<String, Object> bodyData = new HashMap<>();
      Map<String, Object> data = new HashMap<>();
      data.put("serviceType", "MOTORCYCLE");
      data.put("item", item);
      data.put("language", language);
      data.put("stops", stops);
      bodyData.put("data", data);

      Date date = new Date();
      long time = date.getTime();
      String json = gsonObj.toJson(bodyData);
      String signatureData = String.format("%d%nPOST%n/v3/quotations%n%n%s", time, json);
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
    Item item = new Item();
    item.setQuantity(0);
    item.setWeight(0.0);
    for (Product product : products) {
      item.setWeight(item.getWeight() + product.getWeight());
      item.setQuantity(item.getQuantity() + productQuantityMap.get(product.getId()));
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
}
