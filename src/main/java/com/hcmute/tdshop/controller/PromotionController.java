package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.promotion.AddPromotionRequest;
import com.hcmute.tdshop.dto.promotion.UpdatePromotionRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.PromotionService;
import java.time.LocalDateTime;
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
@RequestMapping("/promotion")
public class PromotionController {

  @Autowired
  PromotionService promotionService;

  @GetMapping("/get-all")
  public DataResponse getAll(Pageable pageable) {
    return promotionService.getAll(pageable);
  }

  @GetMapping("/search")
  public DataResponse search(
      @RequestParam(name = "id", required = false, defaultValue = "0") long id,
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "from-rate", required = false, defaultValue = "0") Double fromRate,
      @RequestParam(name = "to-rate", required = false, defaultValue = "0") Double toRate,
      @RequestParam(name = "start-date", required = false) LocalDateTime startDate,
      @RequestParam(name = "end-date", required = false) LocalDateTime endDate,
      Pageable pageable) {
    return promotionService.getPromotion(id, keyword, fromRate, toRate, startDate, endDate, pageable);
  }

  @GetMapping("/get/{id}")
  public DataResponse getById(@PathVariable long id) {
    return promotionService.getById(id);
  }

  @PostMapping("/admin/add")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertPromotion(@RequestBody @Valid AddPromotionRequest request) {
    return promotionService.insertPromotion(request);
  }

  @PutMapping("/admin/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updatePromotion(@PathVariable(name = "id") long id,
      @RequestBody @Valid UpdatePromotionRequest request) {
    return promotionService.updatePromotion(id, request);
  }

  @DeleteMapping("/admin/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deletePromotion(@PathVariable long id) {
    return promotionService.deletePromotion(id);
  }
}
