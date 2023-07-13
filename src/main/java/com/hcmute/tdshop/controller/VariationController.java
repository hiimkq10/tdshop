package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.variation.AddVariationRequest;
import com.hcmute.tdshop.dto.variation.UpdateVariationRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.VariationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/variation")
public class VariationController {

  @Autowired
  VariationService variationService;

  @GetMapping("/get-all")
  public DataResponse getAll(Pageable pageable) {
    return variationService.getAll(pageable);
  }

  @GetMapping("/get")
  public DataResponse getByMasterCategory(@RequestParam(name = "master-category-id") long id) {
    return variationService.getByMasterCategory(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertVariation(@RequestBody @Valid AddVariationRequest request) {
    return variationService.insertVariation(request);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updateVariation(@PathVariable(name = "id") long id,
      @RequestBody @Valid UpdateVariationRequest request) {
    return variationService.updateVariation(id, request);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deleteVariation(@PathVariable(name = "id") long id) {
    return variationService.deleteVariation(id);
  }
}
