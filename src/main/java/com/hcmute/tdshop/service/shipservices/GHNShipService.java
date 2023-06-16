package com.hcmute.tdshop.service.shipservices;

import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.OrderSize;
import com.hcmute.tdshop.dto.shipservices.ProductParameters;
import com.hcmute.tdshop.dto.shipservices.ShipOrderDto;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeDataResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.CancelOrderResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.CreateOrderDataResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.CreateOrderResponse;
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
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShipData;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.enums.GHNShipStatusEnum;
import com.hcmute.tdshop.enums.PaymentMethodEnum;
import com.hcmute.tdshop.mapper.OrderMapper;
import com.hcmute.tdshop.mapper.ShipServicesMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AddressRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ShipDataRepository;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
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

@Service
public class GHNShipService {

  Logger logger = LoggerFactory.getLogger(LalamoveShipService.class);

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  WardsRepository wardsRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ShipServicesMapper shipServicesMapper;

  @Autowired
  ShipDataRepository shipDataRepository;

  @Autowired
  OrderMapper orderMapper;

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

  public boolean checkProductsSize(OrderSize orderSize) {
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

  public boolean checkAllowCancelOrder(String statusCode) {
    if (cancelAllowedStatus.contains(statusCode)) {
      return true;
    }
    return false;
  }

  public boolean checkCodAmount(Set<OrderDetail> setOfOrderDetails) {
    double total = 0.0;
    for (OrderDetail orderDetail : setOfOrderDetails) {
      total += orderDetail.getFinalPrice() * orderDetail.getQuantity();
    }
    if (total >= maxCodAmount) {
      return true;
    }
    return false;
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
      if (!(provinceResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      List<ProvinceDto> dtos = provinceResponse.getData().stream()
          .filter(item -> item.getNameExtension().stream()
              .anyMatch(name -> name.toLowerCase().trim().equals(provinceName.toLowerCase().trim())))
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
      if (!(districtResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      List<DistrictDto> dtos = districtResponse.getData().stream()
          .filter(item -> item.getNameExtension().stream()
              .anyMatch(name -> name.toLowerCase().trim().equals(districtName.toLowerCase().trim())))
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
      if (!(wardsResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      List<WardsDto> dtos = wardsResponse.getData().stream()
          .filter(item -> item.getNameExtension().stream()
              .anyMatch(name -> name.toLowerCase().trim().equals(wardsName.toLowerCase().trim())))
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

  public ShipOrderDto getShipOrderDto(ShopOrder order) {
    GetOrderData getOrderData = getOrder(order);
    return shipServicesMapper.GHNGetOrderDataToShipOrderDto(getOrderData);
  }

  public GetOrderData getOrder(ShopOrder order) {
    try {
      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/v2/shipping-order/detail").build());

      Optional<ShipData> optionalData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null)
          .findFirst();
      if (!optionalData.isPresent()) {
        return null;
      }
      ShipData shipData = optionalData.get();

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("order_code", shipData.getShipOrderId());
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<GetOrderResponse> response = bodySpec.retrieve().bodyToMono(GetOrderResponse.class);
      GetOrderResponse getOrderResponse = response.block();
      if (!(getOrderResponse.getCode() == 200)) {
        return null;
      }

      return getOrderResponse.getData();
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

  public DataResponse createOrder(ShopOrder order, OrderSize orderSize) {
    try {
      Optional<ShipData> optionalShipData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null)
          .findFirst();
      if (optionalShipData.isPresent()) {
        GetOrderData getOrderData = getOrder(order);
        if (!(getOrderData == null || getOrderData.getLogs().get(getOrderData.getLogs().size() - 1).getStatus().equals(
            GHNShipStatusEnum.GHN_CANCEL.getCode()))) {
          return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_EXISTED,
              ApplicationConstants.SHIP_DATA_ORDER_EXISTED_CODE);
        }
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

      bodyMap.put("length", orderSize.getLength());
      bodyMap.put("width", orderSize.getWidth());
      bodyMap.put("height", orderSize.getHeight());
      bodyMap.put("weight", orderSize.getWeight());

      bodyMap.put("insurance_value", total > maxInsuranceValue ? maxInsuranceValue : Math.round(total));

      bodyMap.put("service_type_id", 2);
      bodyMap.put("payment_type_id", 1);
      bodyMap.put("required_note", "CHOXEMHANGKHONGTHU");
      bodyMap.put("items", productParameters);
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<CreateOrderResponse> response = bodySpec.retrieve().bodyToMono(CreateOrderResponse.class);
      CreateOrderResponse createOrderResponse = response.block();
      if (!(createOrderResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }

      CreateOrderDataResponse createOrderDataResponse = createOrderResponse.getData();

      // Save Ship Data
      List<ShipData> shipDatas = shipDataRepository.findByOrder_Id(order.getId());
      LocalDateTime now = LocalDateTime.now();
      int size = shipDatas.size();
      for (int i = 0; i < size; i++) {
        if (shipDatas.get(i).getDeletedAt() == null) {
          shipDatas.get(i).setDeletedAt(now);
        }
      }
      shipDatas.add(new ShipData(null, createOrderDataResponse.getOrderCode(), now, null, order));
      shipDataRepository.saveAllAndFlush(shipDatas);
      ShipOrderDto shipOrderDto = getShipOrderDto(order);
      return new DataResponse(orderMapper.OrderToOrderWithShipDataResponse(order, shipOrderDto, true));
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
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.SHIP_DATA_ORDER_NOT_FOUND,
            ApplicationConstants.SHIP_DATA_ORDER_NOT_FOUND_CODE);
      }

      if (getOrderData.getLogs() != null && getOrderData.getLogs().size() > 0) {
        if (!cancelAllowedStatus.contains(getOrderData.getLogs().get(getOrderData.getLogs().size() - 1).getStatus())) {
          return new DataResponse(ApplicationConstants.BAD_REQUEST,
              ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW,
              ApplicationConstants.SHIP_DATA_ORDER_CANCEL_NOT_ALLOW_CODE);
        }
      }

      Optional<ShipData> optionalData = order.getShipData().stream().filter(sD -> sD.getDeletedAt() == null)
          .findFirst();
      if (!optionalData.isPresent()) {
        ShipOrderDto shipOrderDto = getShipOrderDto(order);
        return new DataResponse(orderMapper.OrderToOrderWithShipDataResponse(order, shipOrderDto, false));
      }
      ShipData shipData = optionalData.get();

      WebClient client = WebClient.builder().baseUrl(baseUrl)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader("Token", token).build();
      UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
      RequestBodySpec bodySpec = uriSpec.uri(
          uriBuilder -> uriBuilder.path("/shiip/public-api/v2/switch-status/cancel").build());

      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("order_codes", Arrays.asList(shipData.getShipOrderId()));
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<CancelOrderResponse> response = bodySpec.retrieve().bodyToMono(CancelOrderResponse.class);
      CancelOrderResponse cancelOrderResponse = response.block();
      if (!(cancelOrderResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      ShipOrderDto shipOrderDto = getShipOrderDto(order);
      return new DataResponse(orderMapper.OrderToOrderWithShipDataResponse(order, shipOrderDto, false));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
  }

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
//      bodyMap.put("length", parameters.getLength());
//      bodyMap.put("width", parameters.getWidth());
//      bodyMap.put("height", parameters.getHeight());
      bodyMap.put("weight", parameters.getWeight());
      bodySpec.body(BodyInserters.fromValue(bodyMap));

      Mono<CalculateFeeDataResponse> response = bodySpec.retrieve().bodyToMono(CalculateFeeDataResponse.class);
      CalculateFeeDataResponse calculateFeeDataResponse = response.block();
      if (!(calculateFeeDataResponse.getCode() == 200)) {
        throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
      }
      return new DataResponse(shipServicesMapper.GHNCalculateFeeDataResponseToFeeResponse(calculateFeeDataResponse));
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
      length = product.getLength();
      width = product.getWidth();
      weight = product.getWeight() * quantity;
      height = product.getHeight() * quantity;

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
    double total = 0;
    for (OrderDetail orderDetail : order.getSetOfOrderDetails()) {
      total += orderDetail.getFinalPrice() * orderDetail.getQuantity();
    }
    return total;
  }
}
