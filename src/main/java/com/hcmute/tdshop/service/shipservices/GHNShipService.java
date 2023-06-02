package com.hcmute.tdshop.service.shipservices;

import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.dto.shipservices.CalculateFeeDto;
import com.hcmute.tdshop.dto.shipservices.ProductParameters;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeDataResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.DistrictDto;
import com.hcmute.tdshop.dto.shipservices.ghn.DistrictResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.ProvinceDto;
import com.hcmute.tdshop.dto.shipservices.ghn.ProvinceResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.WardsDto;
import com.hcmute.tdshop.dto.shipservices.ghn.WardsResponse;
import com.hcmute.tdshop.entity.Address;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AddressRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
  @Autowired
  AddressRepository addressRepository;

  @Autowired
  WardsRepository wardsRepository;

  @Autowired
  ProductRepository productRepository;

  @Value("${ghn.api.token}")
  String token;

  @Value("${ghn.base.url}")
  String baseUrl;

  @Value("${ghn.shop.id}")
  String shopId;

  public ProvinceDto getProvince(String provinceName) {
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
        .filter(item -> item.getProvinceName().toLowerCase().trim().contains(provinceName.toLowerCase().trim()))
        .collect(
            Collectors.toList());
    if (dtos.size() <= 0) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    return dtos.get(0);
  }

  public DistrictDto getDistrict(String districtName, long provinceId) {
    WebClient client = WebClient.builder().baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader("Token", token).build();
    UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
    RequestBodySpec bodySpec = uriSpec.uri(
        uriBuilder -> uriBuilder.path("/shiip/public-api/master-data/district").build());

    Map<String, String> bodyMap = new HashMap<>();
    bodyMap.put("province_id", String.valueOf(provinceId));
    bodySpec.body(BodyInserters.fromValue(bodyMap));

    Mono<DistrictResponse> response = bodySpec.retrieve().bodyToMono(DistrictResponse.class);
    DistrictResponse districtResponse = response.block();
    if (!(districtResponse.getCode() == 200)) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    List<DistrictDto> dtos = districtResponse.getData().stream()
        .filter(item -> item.getDistrictName().toLowerCase().trim().contains(districtName.toLowerCase().trim()))
        .collect(
            Collectors.toList());
    if (dtos.size() <= 0) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    return dtos.get(0);
  }

  public WardsDto getWards(String wardsName, long districtId) {
    WebClient client = WebClient.builder().baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader("Token", token).build();
    UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
    RequestBodySpec bodySpec = uriSpec.uri(
        uriBuilder -> uriBuilder.path("/shiip/public-api/master-data/ward").build());

    Map<String, String> bodyMap = new HashMap<>();
    bodyMap.put("district_id", String.valueOf(districtId));
    bodySpec.body(BodyInserters.fromValue(bodyMap));

    Mono<WardsResponse> response = bodySpec.retrieve().bodyToMono(WardsResponse.class);
    WardsResponse wardsResponse = response.block();
    if (!(wardsResponse.getCode() == 200)) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    List<WardsDto> dtos = wardsResponse.getData().stream()
        .filter(item -> item.getWardName().toLowerCase().trim().contains(wardsName.toLowerCase().trim()))
        .collect(
            Collectors.toList());
    if (dtos.size() <= 0) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    return dtos.get(0);
  }

  public DataResponse createOrder(ShopOrder order) {
    WebClient client = WebClient.builder().baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader("Token", token)
        .defaultHeader("Token", token)
        .defaultHeader("ShopId", shopId).build();
    UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
    RequestBodySpec bodySpec = uriSpec.uri(
        uriBuilder -> uriBuilder.path("/shiip/public-api/v2/shipping-order/create").build());

    Address address = order.getAddress();
    ProvinceDto provinceDto = getProvince(address.getWards().getDistrict().getProvince().getName());
    DistrictDto districtDto = getDistrict(address.getWards().getDistrict().getName(), provinceDto.getProvinceID());
    WardsDto wardsDto = getWards(address.getWards().getName(), districtDto.getDistrictID());
    ProductParameters parameters = calculateProductsParameters3(order.getSetOfOrderDetails());
    List<ProductParameters> productParameters = calculateProductsParameters2(order.getSetOfOrderDetails());

    Map<String, Object> bodyMap = new HashMap<>();
    bodyMap.put("to_name", address.getName());
    bodyMap.put("to_phone", address.getPhone());
    bodyMap.put("to_address", address.getAddressDetail());
    bodyMap.put("to_ward_name", wardsDto.getWardName());
    bodyMap.put("to_district_name", districtDto.getDistrictName());
    bodyMap.put("to_province_name", provinceDto.getProvinceName());
    bodyMap.put("length", String.valueOf(parameters.getLength()));
    bodyMap.put("width", String.valueOf(parameters.getWidth()));
    bodyMap.put("height", String.valueOf(parameters.getHeight()));
    bodyMap.put("weight", String.valueOf(parameters.getWeight()));
    bodyMap.put("service_type_id", "2");
    bodyMap.put("payment_type_id", "1");
    bodyMap.put("required_note", "CHOXEMHANGKHONGTHU");
    bodyMap.put("item", productParameters);
    bodySpec.body(BodyInserters.fromValue(bodyMap));

    Mono<WardsResponse> response = bodySpec.retrieve().bodyToMono(WardsResponse.class);
    WardsResponse wardsResponse = response.block();
    if (!(wardsResponse.getCode() == 200)) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    return DataResponse.SUCCESSFUL;
  }

  public CalculateFeeDataResponse calculateFee(CalculateFeeDto dto) {
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

    Map<String, String> bodyMap = new HashMap<>();
    bodyMap.put("to_ward_code", String.valueOf(wardsDto.getWardCode()));
    bodyMap.put("to_district_id", String.valueOf(districtDto.getDistrictID()));
    bodyMap.put("length", String.valueOf(parameters.getLength()));
    bodyMap.put("width", String.valueOf(parameters.getWidth()));
    bodyMap.put("height", String.valueOf(parameters.getHeight()));
    bodyMap.put("weight", String.valueOf(parameters.getWeight()));
    bodySpec.body(BodyInserters.fromValue(bodyMap));

    Mono<CalculateFeeDataResponse> response = bodySpec.retrieve().bodyToMono(CalculateFeeDataResponse.class);
    CalculateFeeDataResponse calculateFeeDataResponse = response.block();
    if (!(calculateFeeDataResponse.getCode() == 200)) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    return calculateFeeDataResponse;
  }

  public ProductParameters calculateProductsParameters3(Set<OrderDetail> orderDetails) {
    Map<Long, Integer> productQuantityMap = new HashMap<>();
    for (OrderDetail dto : orderDetails) {
      productQuantityMap.put(dto.getProduct().getId(), dto.getQuantity());
    }
    List<Product> products = orderDetails.stream().map(OrderDetail::getProduct).collect(Collectors.toList());
    ProductParameters parameters = new ProductParameters();
    for (Product product : products) {
      Integer quantity = productQuantityMap.get(product.getId());
      if (quantity == null) {
        quantity = 0;
      }
      parameters.setName(product.getName());
      parameters.setQuantity(quantity);
      parameters.setLength(parameters.getLength() + product.getLength() * quantity);
      parameters.setWidth(parameters.getWidth() + product.getWidth() * quantity);
      parameters.setWeight(parameters.getWeight() + product.getWeight() * quantity);
      parameters.setHeight(parameters.getHeight() + product.getHeight() * quantity);
    }
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
    for (Product product : products) {
      Integer quantity = productQuantityMap.get(product.getId());
      if (quantity == null) {
        quantity = 0;
      }
      parameters.setName(product.getName());
      parameters.setQuantity(quantity);
      parameters.setLength(product.getLength() * quantity);
      parameters.setWidth(product.getWidth() * quantity);
      parameters.setWeight(product.getWeight() * quantity);
      parameters.setHeight(product.getHeight() * quantity);
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
    for (Product product : products) {
      Integer quantity = productQuantityMap.get(product.getId());
      if (quantity == null) {
        quantity = 0;
      }
      parameters.setName(product.getName());
      parameters.setQuantity(quantity);
      parameters.setLength(parameters.getLength() + product.getLength() * quantity);
      parameters.setWidth(parameters.getWidth() + product.getWidth() * quantity);
      parameters.setWeight(parameters.getWeight() + product.getWeight() * quantity);
      parameters.setHeight(parameters.getHeight() + product.getHeight() * quantity);
    }
    return parameters;
  }
}
