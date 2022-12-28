package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.ProductPromotion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, Long> {
  List<ProductPromotion> findByProductId(long id);
}
