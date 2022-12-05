package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.AttributeSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeSetRepository extends JpaRepository<AttributeSet, Long> {
  boolean existsByNameIgnoreCase(String name);
}
