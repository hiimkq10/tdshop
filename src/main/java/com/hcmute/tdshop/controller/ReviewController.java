package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.review.AddReviewRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.ReviewService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class ReviewController {

  @Autowired
  private ReviewService reviewService;

  @GetMapping("/get-all")
  public DataResponse getAll(Pageable page) {
    return reviewService.getAll(page);
  }

  @GetMapping("/search")
  public DataResponse search(
      @RequestParam(name = "user-id", required = false, defaultValue = "0") long userId,
      @RequestParam(name = "product-id", required = false, defaultValue = "0") long productId,
      @RequestParam(name = "from-date", required = false) String fromDateTime,
      @RequestParam(name = "to-date", required = false) String toDateTime,
      @RequestParam(name = "is-verified", required = false) Boolean isVerified,
      @RequestParam(name = "is-valid", required = false) Boolean isValid,
      Pageable page
  ) {
    return reviewService.searchReview(userId, productId, fromDateTime, toDateTime, isVerified, isValid, page);
  }

  @GetMapping("/search-all")
  public DataResponse searchAll(
      @RequestParam(name = "user-id", required = false, defaultValue = "0") long userId,
      @RequestParam(name = "product-id", required = false, defaultValue = "0") long productId,
      @RequestParam(name = "from-date", required = false) String fromDateTime,
      @RequestParam(name = "to-date", required = false) String toDateTime,
      @RequestParam(name = "is-verified", required = false) Boolean isVerified,
      @RequestParam(name = "is-valid", required = false) Boolean isValid
  ) {
    return reviewService.searchAll(userId, productId, fromDateTime, toDateTime, isVerified, isValid);
  }

  @PostMapping("/add")
  public DataResponse addReview(@RequestBody @Valid AddReviewRequest request) {
    return reviewService.addReview(request);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deleteReview(@PathVariable(name = "id") long id) {
    return reviewService.deleteReview(id);
  }

  @PostMapping("/accept/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse acceptReview(@PathVariable(name = "id") long id) {
    return reviewService.acceptReview(id);
  }

  @PostMapping("/deny/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse denyReview(@PathVariable(name = "id") long id) {
    return reviewService.denyReview(id);
  }

  @GetMapping("/check-buy")
  public DataResponse checkBuy(@RequestParam(name = "product-id") long productId) {
    return reviewService.checkUserBoughtProduct(productId);
  }

  @GetMapping("/product-avg")
  public DataResponse getProductAvgReview(@RequestParam(name = "product-id") long productId) {
    return reviewService.getProductAvgReview(productId);
  }

  @PostMapping("/mobile-add")
  public DataResponse addReviewForMobile(@RequestBody @Valid AddReviewRequest request) {
    return reviewService.addReviewForMobile(request);
  }

  @DeleteMapping("/mobile-delete/{id}")
  public DataResponse deleteReviewForMobile(@PathVariable(name = "id") long id) {
    return reviewService.deleteReview(id);
  }
}
