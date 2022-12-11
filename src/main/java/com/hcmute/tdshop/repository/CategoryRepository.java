package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  long countByMasterCategory_Id(Long id);
  boolean existsByNameIgnoreCase(String name);
  boolean existsByParent_Id(Long id);
  List<Category> findByMasterCategory_IdAndParentIsNull(Long id);
}
