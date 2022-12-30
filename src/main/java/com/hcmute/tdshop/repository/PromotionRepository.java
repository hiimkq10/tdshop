package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Promotion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {
  Optional<Promotion> findByIdAndDeletedAtIsNull(Long id);
}
