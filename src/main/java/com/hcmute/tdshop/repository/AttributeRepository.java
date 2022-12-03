package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Attribute;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
  long countById(Long id);
}
