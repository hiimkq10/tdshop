package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.shipservices.FeeResponse;
import com.hcmute.tdshop.dto.shipservices.ShipOrderDto;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeDataResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.GetOrderData;
import com.hcmute.tdshop.dto.shipservices.lalamove.QuotationDto;
import com.hcmute.tdshop.enums.GHNShipStatusEnum;
import com.hcmute.tdshop.enums.LalamoveShipStatusEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ShipServicesMapper {
  public abstract FeeResponse GHNCalculateFeeDataResponseToFeeResponse(CalculateFeeDataResponse data);

  public FeeResponse LalamoveQuotationDtoToFeeResponse(QuotationDto data) {
    if (data == null) {
      return null;
    }
    FeeResponse feeResponse = new FeeResponse();
    feeResponse.setTotal(data.getPriceBreakdown().getTotal());
    return feeResponse;
  }

  public ShipOrderDto GHNGetOrderDataToShipOrderDto(GetOrderData data) {
    if (data == null) {
      return new ShipOrderDto("", "");
    }
    ShipOrderDto shipOrderDto = new ShipOrderDto();
    GHNShipStatusEnum shipStatusEnum = GHNShipStatusEnum.getShipStatusEnumByCode(data.getLogs().get(data.getLogs().size() - 1).getStatus());
    shipOrderDto.setStatusCode(shipStatusEnum.getCode());
    shipOrderDto.setStatusDescription(shipStatusEnum.getDescription());

    return shipOrderDto;
  }

  public ShipOrderDto LalamoveGetOrderDataToShipOrderDto(com.hcmute.tdshop.dto.shipservices.lalamove.GetOrderData data) {
    if (data == null) {
      return new ShipOrderDto("", "");
    }
    ShipOrderDto shipOrderDto = new ShipOrderDto();
    LalamoveShipStatusEnum shipStatusEnum = LalamoveShipStatusEnum.getShipStatusEnumByCode(data.getStatus());
    shipOrderDto.setStatusCode(shipStatusEnum.getCode());
    shipOrderDto.setStatusDescription(shipStatusEnum.getDescription());

    return shipOrderDto;
  }
}
