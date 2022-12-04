package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOption, Long> {
  boolean existsByVariation_Id(Long id);
}
