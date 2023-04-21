package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface StatisticService {

  DataResponse orderStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type);
  DataResponse revenueStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type);
  DataResponse ratingStatistic(LocalDateTime fromDate, LocalDateTime toDate);
  DataResponse productStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type);
  DataResponse orderAvarage(LocalDateTime fromDate, LocalDateTime toDate, Integer type);
  DataResponse accountAvarage(LocalDateTime fromDate, LocalDateTime toDate, Long role);
  DataResponse ratingByStar(LocalDateTime fromDate, LocalDateTime toDate);

  DataResponse totalProduct();
  DataResponse totalOrder();
  DataResponse totalRevenue();
  DataResponse ratingAvarage();
}
