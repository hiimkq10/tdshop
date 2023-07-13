package com.hcmute.tdshop.service.shipservices;

import com.google.gson.Gson;
import com.hcmute.tdshop.dto.ResultDto;
import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.dto.shipservices.CalculateDeliveryTimeRequest;
import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.CancelOrderRequest;
import com.hcmute.tdshop.dto.shipservices.CheckShipConditionDto;
import com.hcmute.tdshop.dto.shipservices.CreateOrderRequest;
import com.hcmute.tdshop.dto.shipservices.DeliveryTimeDto;
import com.hcmute.tdshop.dto.shipservices.OrderSize;
import com.hcmute.tdshop.dto.shipservices.ProductParameters;
import com.hcmute.tdshop.dto.shipservices.ShipOrderDto;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeData;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.CancelOrderResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.CreateOrderDataResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.CreateOrderResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.DeliveryTimeData;
import com.hcmute.tdshop.dto.shipservices.ghn.DeliveryTimeResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.DistrictDto;
import com.hcmute.tdshop.dto.shipservices.ghn.DistrictResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.GetOrderData;
import com.hcmute.tdshop.dto.shipservices.ghn.GetOrderResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.ProvinceDto;
import com.hcmute.tdshop.dto.shipservices.ghn.ProvinceResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.WardsDto;
import com.hcmute.tdshop.dto.shipservices.ghn.WardsResponse;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.OrderStatus;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShipData;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.entity.Wards;
import com.hcmute.tdshop.enums.GHNShipStatusEnum;
import com.hcmute.tdshop.enums.OrderStatusEnum;
import com.hcmute.tdshop.enums.PaymentMethodEnum;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.OrderStatusRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

@Service("GHNShipService")
public class GHNShipService extends ShipServices {

  Logger logger = LoggerFactory.getLogger(GHNShipService.class);

  @Autowired
  private OrderStatusRepository orderStatusRepository;

  @Autowired
  private ShopOrderRepository shopOrderRepository;

  @Value("${ghn.api.token}")
  String token;

  @Value("${ghn.base.url}")
  String baseUrl;

  @Value("${ghn.shop.id}")
  String shopId;

  @Value("${ghn.order.cancel.allowed-status}")
  List<String> cancelAllowedStatus;

  @Value("${ghn.order.max-cod-amount}")
  double maxCodAmount;

  @Value("${ghn.order.max-length}")
  double maxLength;

  @Value("${ghn.order.max-width}")
  double maxWidth;

  @Value("${ghn.order.max-height}")
  double maxHeight;

  @Value("${ghn.order.max-weight}")
  double maxWeight;

  @Value("${ghn.order.max-insurance-value}")
  long maxInsuranceValue;

  List<String> allowCancelShopOrderStatus = Arrays.asList(
      GHNShipStatusEnum.GHN_NOT_CREATED.getCode(),
      GHNShipStatusEnum.GHN_CANCEL.getCode()
  );

  @Override
  public boolean checkSize(ShopOrder order) {
    ProductParameters parameters = calculateProductsParameters3(order.getSetOfOrderDetails());
    if (parameters.getWeight() > maxWeight) {
      return false;
    }
    return true;
  }

  @Override
  public boolean checkProductSize(OrderSize orderSize) {
    if (
        orderSize.getLength() > maxLength ||
            orderSize.getWidth() > maxWidth ||
            orderSize.getHeight() > maxHeight ||
            orderSize.getWeight() > maxWeight
    ) {
      return false;
    }
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
    double total = calculateOrderTotal(order);
    if (total >= maxCodAmount) {
      return false;
    }
    return true;
  }

  @Override
  public boolean checkRegion(Address address) {
    return true;
  }

  public ProvinceDto getProvince(String provinceName) {
    try {
      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/master-data/province").build());
      Mono<ProvinceResponse> response = bodySpec.retrieve().bodyToMono(ProvinceResponse.class);
      ProvinceResponse provinceResponse = response.block();
      if (provinceResponse == null || provinceResponse.getCode() != 200) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      List<ProvinceDto> dtos = provinceResponse.getData().stream()
          .filter(item -> item.getNameExtension() != null && (
              item.getProvinceName().toLowerCase().trim().equals(provinceName.toLowerCase().trim())
                  || item.getNameExtension().stream()
                  .anyMatch(name -> name.toLowerCase().trim().equals(provinceName.toLowerCase().trim()))))
          .collect(
              Collectors.toList());
      if (dtos.size() <= 0) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      return dtos.get(0);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  public DistrictDto getDistrict(String districtName, long provinceId) {
    try {
      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/master-data/district").build());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("province_id", provinceId);
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<DistrictResponse> response = bodySpec.retrieve().bodyToMono(DistrictResponse.class);
      DistrictResponse districtResponse = response.block();
      if (districtResponse == null || districtResponse.getCode() != 200) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      List<DistrictDto> dtos = districtResponse.getData().stream()
          .filter(item -> item.getNameExtension() != null && (
              item.getDistrictName().toLowerCase().trim().equals(districtName.toLowerCase().trim())
                  || item.getNameExtension().stream()
                  .anyMatch(name -> name.toLowerCase().trim().equals(districtName.toLowerCase().trim()))))
          .collect(
              Collectors.toList());
      if (dtos.size() <= 0) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      return dtos.get(0);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  public WardsDto getWards(String wardsName, long districtId) {
    try {
      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/master-data/ward").build());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("district_id", districtId);
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<WardsResponse> response = bodySpec.retrieve().bodyToMono(WardsResponse.class);
      WardsResponse wardsResponse = response.block();
      if (wardsResponse == null || wardsResponse.getCode() != 200) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      List<WardsDto> dtos = wardsResponse.getData().stream()
          .filter(item -> item.getNameExtension() != null && (
              item.getWardName().toLowerCase().trim().equals(wardsName.toLowerCase().trim()) || item.getNameExtension()
                  .stream()
                  .anyMatch(name -> name.toLowerCase().trim().equals(wardsName.toLowerCase().trim()))))
          .collect(
              Collectors.toList());
      if (dtos.size() <= 0) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      return dtos.get(0);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  @Override
  public ShipOrderDto getShipOrder(ShopOrder order) {
    GetOrderData getOrderData = getOrder(order);
    return shipServicesMapper.GHNGetOrderDataToShipOrderDto(getOrderData);
  }

  public GetOrderData getOrder(ShopOrder order) {
    try {
      Optional<ShipData> optionalData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null)
          .findFirst();
      if (!optionalData.isPresent()) {
        return null;
      }
      ShipData shipData = optionalData.get();

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/v2/shipping-order/detail").build());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("order_code", shipData.getShipOrderId());
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<GetOrderResponse> response = bodySpec.retrieve().bodyToMono(GetOrderResponse.class);
      GetOrderResponse getOrderResponse = response.block();
      if (getOrderResponse == null || getOrderResponse.getCode() != 200) {
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
    if (!checkProductSize(orderSize)) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ORDER_SIZE_EXCEED_SUPPORT_SIZE,
          ApplicationConstants.ORDER_SIZE_EXCEED_SUPPORT_SIZE_CODE);
    }

    try {
      GetOrderData getOrderData = getOrder(order);
      if (!(getOrderData == null || getOrderData.getStatus().equals(
          GHNShipStatusEnum.GHN_CANCEL.getCode()))) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_EXISTED,
            ApplicationConstants.SHIP_DATA_ORDER_EXISTED_CODE);
      }

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token)
          .defaultHeader("ShopId", shopId).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.post();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/v2/shipping-order/create").build());

      Address address = order.getAddress();
      ProvinceDto provinceDto = getProvince(address.getWards().getDistrict().getProvince().getName());
      DistrictDto districtDto = getDistrict(address.getWards().getDistrict().getName(), provinceDto.getProvinceID());
      WardsDto wardsDto = getWards(address.getWards().getName(), districtDto.getDistrictID());
      List<ProductParameters> productParameters = calculateProductsParameters2(order.getSetOfOrderDetails());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("to_name", address.getName());
      bodyMap.put("to_phone", address.getPhone());
      bodyMap.put("to_address", address.getAddressDetail());
      bodyMap.put("to_ward_name", wardsDto.getWardName());
      bodyMap.put("to_district_name", districtDto.getDistrictName());
      bodyMap.put("to_province_name", provinceDto.getProvinceName());

      double total = calculateOrderTotal(order);
      if (order.getPaymentMethod().getId() == PaymentMethodEnum.COD.getId()) {
        bodyMap.put("cod_amount", Math.round(total));
      }

      bodyMap.put("length", Math.round(orderSize.getLength()));
      bodyMap.put("width", Math.round(orderSize.getWidth()));
      bodyMap.put("height", Math.round(orderSize.getHeight()));
      bodyMap.put("weight", Math.round(orderSize.getWeight()));

      bodyMap.put("insurance_value", total > maxInsuranceValue ? maxInsuranceValue : Math.round(total));

      bodyMap.put("service_type_id", 2);
      bodyMap.put("payment_type_id", 1);
      bodyMap.put("required_note", "CHOXEMHANGKHONGTHU");
      bodyMap.put("items", productParameters);
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Gson gson = new Gson();
      String json = gson.toJson(bodyMap);

      Mono<CreateOrderResponse> response = bodySpec.retrieve().bodyToMono(CreateOrderResponse.class);
      CreateOrderResponse createOrderResponse = response.block();
      if (!(createOrderResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }

      CreateOrderDataResponse createOrderDataResponse = createOrderResponse.getData();

      // Save Ship Data
      List<ShipData> shipDatas = shipDataRepository.findByOrder_IdAndDeletedAtIsNull(order.getId());
      LocalDateTime now = LocalDateTime.now();
      int size = shipDatas.size();
      for (int i = 0; i < size; i++) {
        shipDatas.get(i).setDeletedAt(now);
      }
      shipDatas.add(new ShipData(null, createOrderDataResponse.getOrderCode(), now, null, order));
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
        return new DataResponse(ApplicationConstants.BAD_REQUEST,
            ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW,
            ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW_CODE);
      }

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/v2/switch-status/cancel").build());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("order_codes", Arrays.asList(getOrderData.getOrderCode()));
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<CancelOrderResponse> response = bodySpec.retrieve().bodyToMono(CancelOrderResponse.class);
      CancelOrderResponse cancelOrderResponse = response.block();
      if (cancelOrderResponse.getCode() != 200) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }

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
    try {
      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token)
          .defaultHeader("ShopId", shopId).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/v2/shipping-order/leadtime").build());

      Optional<Address> optionalData = addressRepository.findById(dto.getAddressId());
      if (!optionalData.isPresent()) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      Address address = optionalData.get();

      // Td-shop location
      Optional<Wards> optionalWards = wardsRepository.findById(shopWardsId);
      if (!optionalWards.isPresent()) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      Wards shopWards = optionalWards.get();
      ProvinceDto shopProvinceDto = getProvince(shopWards.getDistrict().getProvince().getName());
      DistrictDto shopDistrictDto = getDistrict(shopWards.getDistrict().getName(), shopProvinceDto.getProvinceID());
      WardsDto shopWardsDto = getWards(shopWards.getName(), shopDistrictDto.getDistrictID());

      ProvinceDto provinceDto = getProvince(address.getWards().getDistrict().getProvince().getName());
      DistrictDto districtDto = getDistrict(address.getWards().getDistrict().getName(), provinceDto.getProvinceID());
      WardsDto wardsDto = getWards(address.getWards().getName(), districtDto.getDistrictID());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("from_district_id", shopDistrictDto.getDistrictID());
      bodyMap.put("from_ward_code", String.valueOf(shopWardsDto.getWardCode()));
      bodyMap.put("to_district_id", districtDto.getDistrictID());
      bodyMap.put("to_ward_code", String.valueOf(wardsDto.getWardCode()));
      bodyMap.put("service_id", 2);
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Gson gson = new Gson();
      String json = gson.toJson(bodyMap);

      Mono<DeliveryTimeResponse> response = bodySpec.retrieve().bodyToMono(DeliveryTimeResponse.class);
      DeliveryTimeResponse deliveryTimeResponse = response.block();
      if (!(deliveryTimeResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }

      DeliveryTimeData deliveryTimeData = deliveryTimeResponse.getData();
      LocalDateTime deliveryTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(deliveryTimeData.getLeadtime() * 1000),
          ZoneId.systemDefault());
      deliveryTime = deliveryTime.plus(orderProgressTime, ChronoUnit.MILLIS);
      return new DataResponse(new DeliveryTimeDto(deliveryTime));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  @Override
  public CheckShipConditionDto checkShipCondition(ShopOrder order, boolean statusCheck) {
    if (statusCheck && order.getOrderStatus().getId() != OrderStatusEnum.PROCCESSING.getId()) {
      return new CheckShipConditionDto(false, ApplicationConstants.ORDER_STATUS_NOT_PROCESSING,
          ApplicationConstants.ORDER_STATUS_NOT_PROCESSING_CODE);
    }
    if (!checkSize(order)) {
      return new CheckShipConditionDto(false, ApplicationConstants.ORDER_SIZE_EXCEED_SUPPORT_SIZE,
          ApplicationConstants.ORDER_SIZE_EXCEED_SUPPORT_SIZE_CODE);
    }
    if (order.getPaymentMethod().getId() == PaymentMethodEnum.COD.getId()) {
      if (!checkCODAmount(order)) {
        return new CheckShipConditionDto(false, ApplicationConstants.ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT,
            ApplicationConstants.ORDER_COD_AMOUNT_EXCEED_SUPPORT_AMOUNT_CODE);
      }
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
        GHNShipStatusEnum.GHN_NOT_CREATED.getCode(),
        GHNShipStatusEnum.GHN_CANCEL.getCode()
    );
    List<String> diliveredStatuses = Arrays.asList(
        GHNShipStatusEnum.GHN_DELIVERED.getCode()
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
    try {
      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token)
          .defaultHeader("ShopId", shopId).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/v2/shipping-order/fee").build());

      Optional<Address> optionalData = addressRepository.findById(dto.getAddressId());
      if (!optionalData.isPresent()) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      Address address = optionalData.get();
      ProvinceDto provinceDto = getProvince(address.getWards().getDistrict().getProvince().getName());
      DistrictDto districtDto = getDistrict(address.getWards().getDistrict().getName(), provinceDto.getProvinceID());
      WardsDto wardsDto = getWards(address.getWards().getName(), districtDto.getDistrictID());
      ProductParameters parameters = calculateProductsParameters(dto.getSetOfProducts());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("service_type_id", 2);
      bodyMap.put("to_ward_code", String.valueOf(wardsDto.getWardCode()));
      bodyMap.put("to_district_id", districtDto.getDistrictID());
      bodyMap.put("weight", parameters.getWeight());
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<CalculateFeeResponse> response = bodySpec.retrieve().bodyToMono(CalculateFeeResponse.class);
      CalculateFeeResponse calculateFeeResponse = response.block();
      if (!(calculateFeeResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      CalculateFeeData calculateFeeData = calculateFeeResponse.getData();
      return new DataResponse(shipServicesMapper.GHNCalculateFeeDataResponseToFeeResponse(calculateFeeData));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  public ProductParameters calculateProductsParameters3(Set<OrderDetail> orderDetails) {
    Map<Long, Integer> productQuantityMap = new HashMap<>();
    for (OrderDetail dto : orderDetails) {
      productQuantityMap.put(dto.getProduct().getId(), dto.getQuantity());
    }
    List<Product> products = orderDetails.stream().map(OrderDetail::getProduct).collect(Collectors.toList());
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

  public List<ProductParameters> calculateProductsParameters2(Set<OrderDetail> orderDetails) {
    Map<Long, Integer> productQuantityMap = new HashMap<>();
    for (OrderDetail dto : orderDetails) {
      productQuantityMap.put(dto.getProduct().getId(), dto.getQuantity());
    }
    List<Product> products = orderDetails.stream().map(OrderDetail::getProduct).collect(Collectors.toList());
    List<ProductParameters> parametersList = new ArrayList<>();
    ProductParameters parameters = null;
    double length = 0.0;
    double height = 0.0;
    double width = 0.0;
    double weight = 0.0;
    for (Product product : products) {
      parameters = new ProductParameters();
      Integer quantity = productQuantityMap.get(product.getId());
      if (quantity == null) {
        quantity = 0;
      }
      parameters.setName(product.getName());
      parameters.setQuantity(quantity);
      length = product.getLength();
      width = product.getWidth();
      weight = product.getWeight();
      height = product.getHeight();

      parameters.setLength(Math.round(length));
      parameters.setWidth(Math.round(width));
      parameters.setHeight(Math.round(height));
      parameters.setWeight(Math.round(weight));

      parametersList.add(parameters);
    }
    return parametersList;
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

  private double calculateOrderTotal(ShopOrder order) {
    double total = order.getShipPrice();
    for (OrderDetail orderDetail : order.getSetOfOrderDetails()) {
      total += orderDetail.getFinalPrice() * orderDetail.getQuantity();
    }
    return total;
  }
}
