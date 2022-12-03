package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.brand.AddBrandRequest;
import com.hcmute.tdshop.dto.brand.UpdateBrandRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BrandService {

  DataResponse getAllBrand(Pageable page);

  DataResponse getBrandById(long id);

  DataResponse insertBrand(AddBrandRequest request, MultipartFile logo);

  DataResponse updateBrand(long id, UpdateBrandRequest request, MultipartFile logo);

  DataResponse deleteBrand(long id);
}
