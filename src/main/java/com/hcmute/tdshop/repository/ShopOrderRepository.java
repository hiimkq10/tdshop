package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.dto.statistic.OrderDto;
import com.hcmute.tdshop.entity.ShopOrder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long>, JpaSpecificationExecutor<ShopOrder> {
  Page<ShopOrder> findByDeletedAtIsNull(Pageable pageable);
  Page<ShopOrder> findByUser_IdAndDeletedAtIsNull(Long userId, Pageable pageable);
  Optional<ShopOrder> findByIdAndUser_IdAndDeletedAtIsNull(Long id, Long userId);

  @Query(value = "SELECT "
      + "new com.hcmute.tdshop.dto.statistic.OrderDto(s.orderStatus.id as id, s.orderStatus.name as name, COUNT(s.orderStatus.id) as amount) "
      + "FROM ShopOrder s "
      + "WHERE s.orderedAt BETWEEN :startDate AND :endDate "
      + "GROUP BY s.orderStatus.id, s.orderStatus.name")
  List<OrderDto> orderStatistic(@Param("startDate") LocalDateTime start,
      @Param("endDate") LocalDateTime end);

  @Query(value = "SELECT "
      + "SUM(o.quantity * o.finalPrice) "
      + "FROM ShopOrder s INNER JOIN OrderDetail o ON s.id = o.order.id "
      + "WHERE s.orderedAt BETWEEN :startDate AND :endDate "
      + "GROUP BY s.id")
  List<Double> orderAVGStatistic(@Param("startDate") LocalDateTime start,
      @Param("endDate") LocalDateTime end);
}
