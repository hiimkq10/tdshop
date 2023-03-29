package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.statistic.OrderDto;
import com.hcmute.tdshop.dto.statistic.RevenueStatisticDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.OrderDetailRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ReviewRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.service.StatisticService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ShopOrderRepository orderRepository;

  @Override
  public DataResponse orderStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    List<OrderDto> orderDtos = orderRepository.orderStatistic(fromDate, toDate);
    double total = 0;
    long size = orderDtos.size();
    for (int i = 0; i < size; i++) {
      total = total + orderDtos.get(i).getAmount();
    }
    for (int i = 0; i < size; i++) {
      orderDtos.get(i).setPercent(String.format("%,.2f", (double) (orderDtos.get(i).getAmount() * 100 / total)));
    }
    return new DataResponse(orderDtos);
  }

  // Type
  // 1: by Date
  // 2: by Month
  // 3: by Year
  @Override
  public DataResponse revenueStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    List<RevenueStatisticDto> revenueStatisticDtos = new ArrayList<>();
    switch (type) {
      case 1:
        revenueStatisticDtos = orderDetailRepository.getRevenueByDate(fromDate, toDate);
        break;
      case 2:
        revenueStatisticDtos = orderDetailRepository.getRevenueByMonth(fromDate, toDate);
        break;
      case 3:
        revenueStatisticDtos = orderDetailRepository.getRevenueByYear(fromDate, toDate);
        break;
      default:
        revenueStatisticDtos = orderDetailRepository.getRevenueByDate(fromDate, toDate);
    }
    int size = revenueStatisticDtos.size();
    double percent = 0;
    double total = 0;
    double sub = 0;
    for (int i = 1; i < size; i++) {
      total = revenueStatisticDtos.get(i).getDRevenue() + revenueStatisticDtos.get(i - 1).getDRevenue();
      sub = revenueStatisticDtos.get(i).getDRevenue() - revenueStatisticDtos.get(i - 1).getDRevenue();
      percent = (sub * 100) / total;
      revenueStatisticDtos.get(i).setPercent(String.format("%,.2f", percent));
    }
    revenueStatisticDtos.get(0).setPercent("0.00");
    return new DataResponse(revenueStatisticDtos);
  }

  @Override
  public DataResponse ratingStatistic(LocalDateTime fromDate, LocalDateTime toDate) {
    return new DataResponse(reviewRepository.ratingStatistic(fromDate, toDate));
  }

  @Override
  public DataResponse productStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    return new DataResponse(productRepository.getProductStatistic(fromDate, toDate));
  }

  @Override
  public DataResponse orderAvarage(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    List<Double> sums = orderRepository.orderAVGStatistic(fromDate, toDate);
    long size = sums.size();
    double total = 0;
    for (int i = 0; i < size; i++) {
      total = total + sums.get(i);
    }
    return new DataResponse(String.format("%,.2f", total / size));
  }

}
