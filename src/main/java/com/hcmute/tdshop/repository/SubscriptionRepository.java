package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  Optional<Subscription> findByUser_IdIsAndProduct_IdIs(Long userId, Long productId);
  List<Subscription> findByProduct_Id(Long productId);
  boolean existsByUser_IdIsAndProduct_IdIs(Long userId, Long productId);
  void deleteByUser_IdIsAndProduct_IdIs(Long userId, Long productId);
}
