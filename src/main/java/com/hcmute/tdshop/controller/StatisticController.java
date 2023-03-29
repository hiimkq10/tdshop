package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.StatisticService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
  public DataResponse revenue(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
      @RequestParam(name = "type", required = false, defaultValue = "1") Integer type
  ) {
    return statisticService.revenueStatistic(fromDate, toDate, type);
  }

  @GetMapping("/rating")
  public DataResponse rating(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.ratingStatistic(fromDate, toDate);
  }

  @GetMapping("/product")
  public DataResponse product(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.productStatistic(fromDate, toDate, 0);
  }

  @GetMapping("/order")
  public DataResponse order(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.orderStatistic(fromDate, toDate, 0);
  }

  @GetMapping("/order-avg")
  public DataResponse orderAvg(
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
      @RequestParam(name = "to-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate
  ) {
    return statisticService.orderAvarage(fromDate, toDate, 0);
  }
}
