package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.StatisticService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

  @Autowired
  StatisticService statisticService;

  @GetMapping("/revenue")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse revenue(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
      @RequestParam(name = "type", required = false, defaultValue = "1") Integer type
  ) {
    return statisticService.revenueStatistic(fromDate, toDate, type);
  }

  @GetMapping("/rating")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse rating(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.ratingStatistic(fromDate, toDate);
  }

  @GetMapping("/product")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse product(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.productStatistic(fromDate, toDate, 0);
  }

  @GetMapping("/order")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse order(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.orderStatistic(fromDate, toDate, 0);
  }

  @GetMapping("/order-avg")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse orderAvg(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.orderAvarage(fromDate, toDate, 0);
  }

  @GetMapping("/account")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse accountAvg(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
      @RequestParam(name = "role", defaultValue = "0") Long role
  ) {
    return statisticService.accountAvarage(fromDate, toDate, role);
  }

  @GetMapping("/rating-by-star")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse ratingByStar(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.ratingByStar(fromDate, toDate);
  }

  @GetMapping("/total-product")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse totalProduct() {
    return statisticService.totalProduct();
  }

  @GetMapping("/total-order")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse totalOrder() {
    return statisticService.totalOrder();
  }

  @GetMapping("/total-revenue")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse totalRevenue() {
    return statisticService.totalRevenue();
  }

  @GetMapping("/rating-avarage")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse ratingAvarage() {
    return statisticService.ratingAvarage();
  }
}
