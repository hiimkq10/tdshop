package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Variation;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationRepository extends JpaRepository<Variation, Long> {
  long countByMasterCategory_Id(Long id);
  boolean existsByNameIgnoreCaseAndMasterCategory_Id(String name, Long id);
  List<Variation> findByMasterCategory_Id(Long id);
}
