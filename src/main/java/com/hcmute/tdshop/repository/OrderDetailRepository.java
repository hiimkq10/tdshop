package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
  boolean existsByProduct_IdAndOrder_User_Id(long productId, long userId);
}
