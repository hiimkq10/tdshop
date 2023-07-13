package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.review.ReviewDto;
import com.hcmute.tdshop.dto.statistic.AccountStatisticDto;
import com.hcmute.tdshop.dto.statistic.OrderDto;
import com.hcmute.tdshop.dto.statistic.RatingByStarDto;
import com.hcmute.tdshop.dto.statistic.RevenueStatisticDto;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ShopOrder;
import com.hcmute.tdshop.enums.AccountRoleEnum;
import com.hcmute.tdshop.enums.OrderStatusEnum;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.OrderDetailRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ReviewRepository;
import com.hcmute.tdshop.repository.ShopOrderRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.ReviewService;
import com.hcmute.tdshop.service.StatisticService;
import com.hcmute.tdshop.specification.OrderSpecification;
import com.hcmute.tdshop.specification.ProductSpecification;
import com.hcmute.tdshop.utils.SpecificationHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReviewService reviewService;

  LocalDateTime maxDate = LocalDateTime.of(3000, 1, 1, 1, 1);
  LocalDateTime minDate = LocalDateTime.of(1970, 1, 1, 1, 1);

  @Override
  public DataResponse orderStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    if (fromDate == null) {
      fromDate = minDate;
    }
    if (toDate == null) {
      toDate = maxDate;
    }
    List<OrderDto> orderDtos = orderRepository.orderStatistic(fromDate, toDate);
    double total = 0;
    long size = orderDtos.size();
    for (int i = 0; i < size; i++) {
      total = total + orderDtos.get(i).getAmount();
    }
    for (int i = 0; i < size; i++) {
      orderDtos.get(i).setPercent(String.format("%,.2f", (double) (orderDtos.get(i).getAmount() * 100 / total)));
    }
    for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
      if (!orderDtos.stream().map(item -> item.getId()).collect(Collectors.toList())
          .contains(orderStatusEnum.getId())) {
        orderDtos.add(new OrderDto(orderStatusEnum.getId(), orderStatusEnum.getName(), 0, String.format("%,.2f", 0.0)));
      }
    }
    return new DataResponse(orderDtos);
  }

  // Type
  // 1: by Date
  // 2: by Month
  // 3: by Year
  @Override
  public DataResponse revenueStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    if (fromDate == null) {
      fromDate = minDate;
    }
    if (toDate == null) {
      toDate = maxDate;
    }
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
    if (fromDate == null) {
      fromDate = minDate;
    }
    if (toDate == null) {
      toDate = maxDate;
    }
    return new DataResponse(reviewRepository.ratingStatistic(fromDate, toDate));
  }

  @Override
  public DataResponse productStatistic(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    if (fromDate == null) {
      fromDate = minDate;
    }
    if (toDate == null) {
      toDate = maxDate;
    }
    return new DataResponse(productRepository.getProductStatistic(fromDate, toDate));
  }

  @Override
  public DataResponse orderAvarage(LocalDateTime fromDate, LocalDateTime toDate, Integer type) {
    if (fromDate == null) {
      fromDate = minDate;
    }
    if (toDate == null) {
      toDate = maxDate;
    }
    List<Double> sums = orderRepository.orderAVGStatistic(fromDate, toDate);
    long size = sums.size();
    double total = 0;
    for (int i = 0; i < size; i++) {
      total = total + sums.get(i);
    }
    return new DataResponse(String.format("%,.2f", total / size));
  }

  @Override
  public DataResponse accountAvarage(LocalDateTime fromDate, LocalDateTime toDate, Long role) {
    if (fromDate == null) {
      fromDate = minDate;
    }
    if (toDate == null) {
      toDate = maxDate;
    }
    List<AccountStatisticDto> dtos = userRepository.accountStatistic(fromDate, toDate, role);
    for (AccountRoleEnum accountRoleEnum : AccountRoleEnum.values()) {
      if (!dtos.stream().map(item -> item.getRoleId()).collect(Collectors.toList()).contains(accountRoleEnum.getId())) {
        dtos.add(new AccountStatisticDto(accountRoleEnum.getId(), accountRoleEnum.getName(), 0l));
      }
    }
    return new DataResponse(dtos);
  }

  @Override
  public DataResponse ratingByStar(LocalDateTime fromDate, LocalDateTime toDate) {
    if (fromDate == null) {
      fromDate = minDate;
    }
    if (toDate == null) {
      toDate = maxDate;
    }
    List<ReviewDto> reviews = reviewService.searchAllList(0, 0, fromDate.toString().replace("T", " "),
        toDate.toString().replace("T", " ").substring(0, 16), true, true);
    List<RatingByStarDto> ratingByStarDtoList = new ArrayList<>();
    RatingByStarDto rating;
    double total = 0;
    long size = reviews.size();
    long star1 = 0;
    long star2 = 0;
    long star3 = 0;
    long star4 = 0;
    long star5 = 0;
    for (int i = 0; i < size; i++) {
      total = total + reviews.get(i).getRatingValue();
      if (reviews.get(i).getRatingValue() < 1.5) {
        star1 += 1;
      } else if (reviews.get(i).getRatingValue() >= 1.5 && reviews.get(i).getRatingValue() < 2.5) {
        star2 += 1;
      } else if (reviews.get(i).getRatingValue() >= 2.5 && reviews.get(i).getRatingValue() < 3.5) {
        star3 += 1;
      } else if (reviews.get(i).getRatingValue() >= 3.5 && reviews.get(i).getRatingValue() < 4.5) {
        star4 += 1;
      } else if (reviews.get(i).getRatingValue() >= 4.5) {
        star5 += 1;
      }
    }
    ratingByStarDtoList.add(new RatingByStarDto(1, star1));
    ratingByStarDtoList.add(new RatingByStarDto(2, star2));
    ratingByStarDtoList.add(new RatingByStarDto(3, star3));
    ratingByStarDtoList.add(new RatingByStarDto(4, star4));
    ratingByStarDtoList.add(new RatingByStarDto(5, star5));
    return new DataResponse(ratingByStarDtoList);
  }

  @Override
  public DataResponse totalProduct() {
    List<Specification<Product>> specifications = new ArrayList<>();
    specifications.add(ProductSpecification.isNotDeleted());
    Specification<Product> conditions = SpecificationHelper.and(specifications);
    return new DataResponse(productRepository.count(conditions));
  }

  @Override
  public DataResponse totalOrder() {
    List<Specification<ShopOrder>> specifications = new ArrayList<>();
    specifications.add(OrderSpecification.isNotDeleted());
    Specification<ShopOrder> conditions = SpecificationHelper.and(specifications);
    return new DataResponse(orderRepository.count(conditions));
  }

  @Override
  public DataResponse totalRevenue() {
    return new DataResponse(String.format("%,.2f", orderDetailRepository.getTotalRevenue()));
  }

  @Override
  public DataResponse ratingAvarage() {
    return new DataResponse(String.format("%,.2f", reviewRepository.ratingAvg()));
  }

}
