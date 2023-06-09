package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.shipservices.FeeResponse;
import com.hcmute.tdshop.dto.shipservices.ghn.CalculateFeeDataResponse;
import com.hcmute.tdshop.dto.shipservices.lalamove.QuotationDto;
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
}
