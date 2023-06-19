package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.address.AddressToLatAndLngDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidResponse;
import com.hcmute.tdshop.dto.address.Coordinate;
import com.hcmute.tdshop.dto.address.Polygon;
import com.hcmute.tdshop.dto.address.ReverseAddressDto;
import com.hcmute.tdshop.entity.Wards;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.service.LocationService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import com.hcmute.tdshop.utils.location.LatLng;
import com.hcmute.tdshop.utils.location.PolyUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;

@Service
public class LocationServiceImpl implements LocationService {
  Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

  @Autowired
  WardsRepository wardsRepository;

  @Override
  public DataResponse addressToLatAndLng(AddressToLatAndLngDto dto) {
    Optional<Wards> optionalData = wardsRepository.findById(dto.getWardsId());
    if (!optionalData.isPresent()) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    Wards wards = optionalData.get();
    for (int i = 1; i <= 4; i++) {
      List<Coordinate> coordinates = searchAddressLatLng(generateAddress(i, wards, dto.getAddressDetail()));
      if (coordinates.size() > 0) {
        return new DataResponse(coordinates.get(0));
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse checkCoordinateValid(CheckCoordinateValidDto dto) {
    Optional<Wards> optionalData = wardsRepository.findById(dto.getWardsId());
    if (!optionalData.isPresent()) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    LatLng chosenPoint = new LatLng(Double.parseDouble(dto.getLat()), Double.parseDouble(dto.getLng()));
    Wards wards = optionalData.get();
    int type = 2;
    boolean result = false;
    List<Polygon> polygons = new ArrayList<>();
    for (int i = 2; i <= 4; i++) {
      polygons = searchPolygon(generateAddress(i, wards, ""));
      if (polygons.size() > 0) {
        type = i;
        break;
      }
    }
    for (List<List<String>> locationCoors : polygons.get(0).getGeojson().getCoordinates()) {
      List<LatLng> latLngs = locationCoors.stream()
          .map(item -> new LatLng(Double.parseDouble(item.get(1)), Double.parseDouble(item.get(0)))).collect(
              Collectors.toList());
      if (PolyUtil.containsLocation(chosenPoint, latLngs, false) || PolyUtil.isLocationOnEdge(chosenPoint, latLngs, false, 300)) {
        result = true;
        break;
      }
    }
    CheckCoordinateValidResponse response = new CheckCoordinateValidResponse();
    response.setResult(result);
    response.setAddressLocation(wards.getName());
    response.setChosenLocation(wards.getName());
    if (!result) {
      ReverseAddressDto reverseAddressDto = reverseAddress(dto.getLat(), dto.getLng());
      switch (type) {
        case 2:
          response.setChosenLocation(reverseAddressDto.getAddress().getSuburb());
          break;
        case 3:
          response.setAddressLocation(wards.getDistrict().getName());
          response.setChosenLocation(reverseAddressDto.getAddress().getCity());
          break;
        case 4:
          response.setAddressLocation(wards.getDistrict().getProvince().getName());
          response.setChosenLocation(reverseAddressDto.getAddress().getState());
          break;
      }
    }
    return new DataResponse(response);
  }

  private List<Polygon> searchPolygon(String address) {
    WebClient client = WebClient.builder().baseUrl("https://nominatim.openstreetmap.org")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
    RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> uriBuilder.path("/search")
        .queryParam("q", address)
        .queryParam("polygon_geojson", 1)
        .queryParam("format", "jsonv2")
        .queryParam("accept-language", "vi").build());
    Mono<List<Polygon>> response = bodySpec.retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Polygon>>() {
        });
    return response.block();
  }

  private List<Coordinate> searchAddressLatLng(String address) {
    WebClient client = WebClient.builder().baseUrl("https://nominatim.openstreetmap.org")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
    RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> uriBuilder.path("/search")
        .queryParam("q", address)
        .queryParam("format", "jsonv2")
        .queryParam("accept-language", "vi").build());
    Mono<List<Coordinate>> response = bodySpec.retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Coordinate>>() {
        });
    return response.block();
  }

  private ReverseAddressDto reverseAddress(String lat, String lon) {
    WebClient client = WebClient.builder().baseUrl("https://nominatim.openstreetmap.org")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    UriSpec<RequestBodySpec> uriSpec = (UriSpec<RequestBodySpec>) client.get();
    RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> uriBuilder.path("/reverse")
        .queryParam("lat", lat)
        .queryParam("lon", lon)
        .queryParam("format", "jsonv2")
        .queryParam("accept-language", "vi").build());
    Mono<ReverseAddressDto> response = bodySpec.retrieve()
        .bodyToMono(ReverseAddressDto.class);
    return response.block();
  }

  // type = 1 with address detail
  // type = 2 only wards district province
  // type = 3 only district province
  // type = 4 only province
  private String generateAddress(int type, Wards wards, String addressDetail) {
    List<String> address = new ArrayList<>();
    switch (type) {
      case 1:
        if (Strings.isNotBlank(addressDetail)) {
          address.add(addressDetail);
        }
      case 2:
        if (Strings.isNotBlank(wards.getName())) {
          address.add(wards.getName());
        }
      case 3:
        if (Strings.isNotBlank(wards.getDistrict().getName())) {
          address.add(wards.getDistrict().getName());
        }
      case 4:
        if (Strings.isNotBlank(wards.getDistrict().getProvince().getName())) {
          address.add(wards.getDistrict().getProvince().getName());
        }
    }
    return String.join(",", address);
  }
}
