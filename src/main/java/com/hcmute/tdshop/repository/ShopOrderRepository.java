package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.ShopOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
  Page<ShopOrder> findByUser_Id(Long userId);
}
