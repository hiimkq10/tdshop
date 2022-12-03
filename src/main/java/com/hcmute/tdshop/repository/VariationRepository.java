package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationRepository extends JpaRepository<Variation, Long> {
  long countByMasterCategory_Id(long id);
}
