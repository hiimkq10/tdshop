package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.VariationOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOption, Long> {
  boolean existsByVariation_Id(Long id);
  List<VariationOption> findByVariation_Id(Long id);
}
