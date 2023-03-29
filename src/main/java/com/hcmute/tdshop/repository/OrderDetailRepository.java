package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.dto.statistic.RevenueStatisticDto;
import com.hcmute.tdshop.entity.OrderDetail;
import com.hcmute.tdshop.model.RevenueStatisticModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

  boolean existsByProduct_IdAndOrder_User_Id(long productId, long userId);

  @Query(value = "SELECT new com.hcmute.tdshop.dto.statistic.RevenueStatisticDto(TO_CHAR(s.orderedAt, 'yyyy-mm-dd') as date, SUM(o.finalPrice * o.quantity) as revenue) "
      + "FROM OrderDetail o JOIN ShopOrder s ON o.order.id = s.id "
      + "WHERE s.orderedAt BETWEEN :startDate AND :endDate "
      + "GROUP BY TO_CHAR(s.orderedAt, 'yyyy-mm-dd')")
  public List<RevenueStatisticDto> getRevenueByDate(@Param("startDate") LocalDateTime start,
      @Param("endDate") LocalDateTime end);

  @Query(value = "SELECT new com.hcmute.tdshop.dto.statistic.RevenueStatisticDto(TO_CHAR(s.orderedAt, 'yyyy-mm') as date, SUM(o.finalPrice * o.quantity) as revenue) "
      + "FROM OrderDetail o JOIN ShopOrder s ON o.order.id = s.id "
      + "WHERE s.orderedAt BETWEEN :startDate AND :endDate "
      + "GROUP BY TO_CHAR(s.orderedAt, 'yyyy-mm')")
  public List<RevenueStatisticDto> getRevenueByMonth(@Param("startDate") LocalDateTime start,
      @Param("endDate") LocalDateTime end);

  @Query(value = "SELECT new com.hcmute.tdshop.dto.statistic.RevenueStatisticDto(TO_CHAR(s.orderedAt, 'yyyy') as date, SUM(o.finalPrice * o.quantity) as revenue) "
      + "FROM OrderDetail o JOIN ShopOrder s ON o.order.id = s.id "
      + "WHERE s.orderedAt BETWEEN :startDate AND :endDate "
      + "GROUP BY TO_CHAR(s.orderedAt, 'yyyy')")
  public List<RevenueStatisticDto> getRevenueByYear(@Param("startDate") LocalDateTime start,
      @Param("endDate") LocalDateTime end);
}
