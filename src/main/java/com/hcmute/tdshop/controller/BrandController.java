package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.brand.AddBrandRequest;
import com.hcmute.tdshop.dto.brand.UpdateBrandRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.BrandService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/brand")
public class BrandController {

  @Autowired
  private BrandService brandService;

  @GetMapping("/get-all")
  public DataResponse getAllBrand(Pageable pageable) {
    return brandService.getAllBrand(pageable);
  }

  @GetMapping("/get/{id}")
  public DataResponse getBrandById(@PathVariable(name = "id") long id) {
    return brandService.getBrandById(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertBrand(@RequestPart(name = "BrandInfo") @Valid AddBrandRequest request,
      @RequestPart(name = "Logo") MultipartFile logo) {
    return brandService.insertBrand(request, logo);
  }

  @PostMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updateBrand(
      @PathVariable(name = "id") long id,
      @RequestPart(name = "BrandInfo") @Valid UpdateBrandRequest request,
      @RequestPart(name = "Logo") MultipartFile logo) {
    return brandService.updateBrand(id, request, logo);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deleteBrand(@PathVariable(name = "id") long id) {
    return brandService.deleteBrand(id);
  }
}
