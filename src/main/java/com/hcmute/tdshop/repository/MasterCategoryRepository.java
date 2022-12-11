package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.MasterCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCategoryRepository extends JpaRepository<MasterCategory, Long> {
  boolean existsByNameIgnoreCase(String name);
}
