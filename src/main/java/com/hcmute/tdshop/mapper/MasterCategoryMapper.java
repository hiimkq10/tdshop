package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.mastercategory.AddMasterCategoryRequest;
import com.hcmute.tdshop.dto.mastercategory.UpdateMasterCategoryRequest;
import com.hcmute.tdshop.entity.MasterCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MasterCategoryMapper {
  public abstract MasterCategory AddMasterCategoryRequestToMasterCategory(AddMasterCategoryRequest request);
  public abstract MasterCategory UpdateMasterCategoryRequestToMasterCategory(UpdateMasterCategoryRequest request);
}
