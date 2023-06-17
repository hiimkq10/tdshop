package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.shipservices.FeeResponse;
import com.hcmute.tdshop.dto.shipservices.ShipOrderDto;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeData;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.GetOrderData;
import com.hcmute.tdshop.dto.shipservices.lalamove.QuotationDto;
import com.hcmute.tdshop.enums.GHNShipStatusEnum;
import com.hcmute.tdshop.enums.LalamoveShipStatusEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ShipServicesMapper {
  public FeeResponse GHNCalculateFeeDataResponseToFeeResponse(CalculateFeeData data) {
    if ( data == null ) {
      return null;
    }

    FeeResponse feeResponse = new FeeResponse();

    feeResponse.setTotal( roundDouble( data.getTotal() ) );

    return feeResponse;
  }

  public FeeResponse LalamoveQuotationDtoToFeeResponse(QuotationDto data) {
    if (data == null) {
      return null;
    }
    FeeResponse feeResponse = new FeeResponse();
    feeResponse.setTotal(roundDouble(data.getPriceBreakdown().getTotal()));
    return feeResponse;
  }

  public ShipOrderDto GHNGetOrderDataToShipOrderDto(GetOrderData data) {
    if (data == null) {
      return new ShipOrderDto(GHNShipStatusEnum.GHN_NOT_CREATED.getCode(), GHNShipStatusEnum.GHN_NOT_CREATED.getDescription());
    }
    ShipOrderDto shipOrderDto = new ShipOrderDto();
    GHNShipStatusEnum shipStatusEnum = GHNShipStatusEnum.getShipStatusEnumByCode(data.getStatus());
    shipOrderDto.setStatusCode(shipStatusEnum.getCode());
    shipOrderDto.setStatusDescription(shipStatusEnum.getDescription());

    return shipOrderDto;
  }

  public ShipOrderDto LalamoveGetOrderDataToShipOrderDto(com.hcmute.tdshop.dto.shipservices.lalamove.GetOrderData data) {
    if (data == null) {
      return new ShipOrderDto(LalamoveShipStatusEnum.LALAMOVE_NOT_CREATED.getCode(), LalamoveShipStatusEnum.LALAMOVE_NOT_CREATED.getDescription());
    }
    ShipOrderDto shipOrderDto = new ShipOrderDto();
    LalamoveShipStatusEnum shipStatusEnum = LalamoveShipStatusEnum.getShipStatusEnumByCode(data.getStatus());
    shipOrderDto.setStatusCode(shipStatusEnum.getCode());
    shipOrderDto.setStatusDescription(shipStatusEnum.getDescription());

    return shipOrderDto;
  }

  public double roundDouble(Double num) {
    return Math.round(num / 1000) * 1000;
  }
}
