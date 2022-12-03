package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.brand.AddBrandRequest;
import com.hcmute.tdshop.dto.brand.UpdateBrandRequest;
import com.hcmute.tdshop.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BrandMapper {

  public abstract Brand AddBrandRequestToBrand(AddBrandRequest request);

  public abstract Brand UpdateBrandRequestToBrand(UpdateBrandRequest request);
}
