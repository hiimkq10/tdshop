package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.address.AddressToLatAndLngDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidDto;
import com.hcmute.tdshop.dto.address.CheckCoordinateValidResponse;
import com.hcmute.tdshop.dto.address.Coordinate;
import com.hcmute.tdshop.dto.address.Geojson;
import com.hcmute.tdshop.dto.address.Polygon;
import com.hcmute.tdshop.dto.address.RawPolygon;
import com.hcmute.tdshop.dto.address.ReverseAddressDto;
import com.hcmute.tdshop.entity.District;
import com.hcmute.tdshop.entity.Province;
import com.hcmute.tdshop.entity.Wards;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.DistrictRepository;
import com.hcmute.tdshop.repository.ProvinceRepository;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.service.LocationService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import com.hcmute.tdshop.utils.location.LatLng;
import com.hcmute.tdshop.utils.location.PolyUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
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
  ProvinceRepository provinceRepository;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  WardsRepository wardsRepository;

  final List<String> ignoredAreaTags = Arrays.asList(
      "bakery",
      "road",
      "construction",
      "continent",
      "house_number",
      "public_building",
      "country",
      "footway",
      "street",
      "farm"
  );

  final List<String> allowedAreaCategories = Arrays.asList("boundary");
  final List<String> allowedTypes = Arrays.asList("administrative");


  @Override
  public DataResponse addressToLatAndLng(AddressToLatAndLngDto dto) {
    Optional<Wards> optionalData = wardsRepository.findById(dto.getWardsId());
    if (!optionalData.isPresent()) {
      throw new RuntimeException(ApplicationConstants.UNEXPECTED_ERROR);
    }
    Wards wards = optionalData.get();
    for (int i = 1; i <= 4; i++) {
      List<Coordinate> coordinates = searchAddressLatLng(
          generateAddress(i, wards.getName(), wards.getDistrict().getName(), wards.getDistrict().getName(),
              dto.getAddressDetail()));
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
    int size = 0;
    Polygon polygon = null;
    for (int i = 2; i <= 4; i++) {
      polygons = searchPolygon(generateAddress(i, wards.getName(), wards.getDistrict().getName(),
          wards.getDistrict().getProvince().getName(), ""));
      size = polygons.size();
      if (size > 0) {
        for (int j = 0; j <= size; j++) {
          Polygon tempPolygon = polygons.get(j);
          if (
              (Strings.isNotBlank(tempPolygon.getCategory()) && allowedAreaCategories.contains(
                  tempPolygon.getCategory()))
                  && (Strings.isNotBlank(tempPolygon.getType()) && allowedTypes.contains(tempPolygon.getType()))
          ) {
            type = i;
            polygon = tempPolygon;
            break;
          }
        }
        if (polygon != null) {
          break;
        }
      }
    }
    if (polygon == null) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.UNEXPECTED_ERROR,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    for (List<List<Double>> locationCoors : polygon.getGeojson().getCoordinates()) {
      List<LatLng> latLngs = locationCoors.stream()
          .map(item -> new LatLng(item.get(1), item.get(0))).collect(
              Collectors.toList());
      if (PolyUtil.containsLocation(chosenPoint, latLngs, false) || PolyUtil.isLocationOnEdge(chosenPoint, latLngs,
          false, 300)) {
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
      List<String> addressList = new ArrayList<>();
      for (Map.Entry<String, String> entry : reverseAddressDto.getAddress().entrySet()) {
        if (!ignoredAreaTags.contains(entry.getKey())) {
          addressList.add(entry.getValue());
        }
      }
      Province cProvince = null;
      District cDistrict = null;
      Wards cWards = null;
      List<Province> provinceList = provinceRepository.findAll();
      Iterator<Province> provinceIterator = provinceList.iterator();
      while (provinceIterator.hasNext()) {
        Province p = provinceIterator.next();
        if (addressList.stream().anyMatch(item -> item.contains(p.getShortName())) && checkContainLocation(chosenPoint,
            p.getName())) {
          cProvince = p;
          break;
        }
      }
      if (cProvince != null) {
        List<District> districtList = districtRepository.findByProvince_Id(cProvince.getId(), Sort.unsorted());
        Iterator<District> districtIterator = districtList.iterator();
        while (districtIterator.hasNext()) {
          District d = districtIterator.next();
          System.out.println(addressList.stream().anyMatch(item -> item.contains(d.getShortName())));
          if (addressList.stream().anyMatch(item -> item.contains(d.getShortName())) && checkContainLocation(
              chosenPoint, generateAddress(3, "", d.getName(), d.getProvince().getName(), ""))) {
            cDistrict = d;
            break;
          }
        }
        if (cDistrict != null) {
          List<Wards> wardsList = wardsRepository.findByDistrict_Id(cDistrict.getId(), Sort.unsorted());
          Iterator<Wards> wardsIterator = wardsList.iterator();
          while (wardsIterator.hasNext()) {
            Wards w = wardsIterator.next();
            if (addressList.stream().anyMatch(item -> item.contains(w.getShortName())) && checkContainLocation(
                chosenPoint,
                generateAddress(2, w.getName(), w.getDistrict().getName(), w.getDistrict().getProvince().getName(),
                    ""))) {
              cWards = w;
            }
          }
        }
      }
      response.setChosenLocation("Không tìm thấy");
      switch (type) {
        case 2:
          if (cWards != null && !cWards.getName().equals(wards.getName())) {
            response.setChosenLocation(cWards.getName());
            break;
          }
        case 3:
          if (cDistrict != null && !cDistrict.getName().equals(wards.getDistrict().getName())) {
            response.setAddressLocation(wards.getDistrict().getName());
            response.setChosenLocation(cDistrict.getName());
            break;
          }
        case 4:
          if (cProvince != null && !cProvince.getName().equals(wards.getDistrict().getProvince().getName())) {
            response.setAddressLocation(wards.getDistrict().getProvince().getName());
            response.setChosenLocation(cProvince.getName());
            break;
          }
          break;
      }
    }
    return new DataResponse(response);
  }

  private boolean checkContainLocation(LatLng chosenPoint, String choosenLocation) {
    int size = 0;
    Polygon polygon = null;
    List<Polygon> polygons = searchPolygon(choosenLocation);
    size = polygons.size();
    if (size <= 0) {
      return false;
    }
    for (int j = 0; j <= size; j++) {
      Polygon tempPolygon = polygons.get(j);
      if (
          (Strings.isNotBlank(tempPolygon.getCategory()) && allowedAreaCategories.contains(tempPolygon.getCategory()))
              && (Strings.isNotBlank(tempPolygon.getType()) && allowedTypes.contains(tempPolygon.getType()))
      ) {
        polygon = tempPolygon;
        break;
      }
    }
    if (polygon == null) {
      return false;
    }
    for (List<List<Double>> locationCoors : polygon.getGeojson().getCoordinates()) {
      List<LatLng> latLngs = locationCoors.stream()
          .map(item -> new LatLng(item.get(1), item.get(0))).collect(
              Collectors.toList());
      if (PolyUtil.containsLocation(chosenPoint, latLngs, false) || PolyUtil.isLocationOnEdge(chosenPoint, latLngs,
          false, 0)) {
        return true;
      }
    }
    return false;
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
    Mono<List<RawPolygon>> response = bodySpec.retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<RawPolygon>>() {
        });
    List<RawPolygon> rawPolygons = response.block();
    if (rawPolygons == null) {
      return new ArrayList<>();
    }
    List<Polygon> polygons = rawPolygons.stream()
        .filter(item -> item.getGeojson() != null && item.getGeojson().getType().equals("Polygon"))
        .map(Polygon::toPolygon).collect(Collectors.toList());
    return polygons;
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
  private String generateAddress(int type, String wardsName, String districtName, String provinceName,
      String addressDetail) {
    List<String> address = new ArrayList<>();
    switch (type) {
      case 1:
        if (Strings.isNotBlank(addressDetail)) {
          address.add(addressDetail);
        }
      case 2:
        if (Strings.isNotBlank(wardsName)) {
          address.add(wardsName);
        }
      case 3:
        if (Strings.isNotBlank(districtName)) {
          address.add(districtName);
        }
      case 4:
        if (Strings.isNotBlank(provinceName)) {
          address.add(provinceName);
        }
    }
    return String.join(",", address);
  }
}
