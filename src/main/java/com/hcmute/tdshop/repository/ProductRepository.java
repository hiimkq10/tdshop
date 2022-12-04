package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Product;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
  Optional<Product> findByIdAndStatus_IdNotInAndDeletedAtNull(Long id, Long[] ids);
  boolean existsByIdAndStatus_IdNotInAndDeletedAtNull(Long id, Long[] ids);
  Set<Product> findByIdIn(Set<Long> ids);
  long countByBrand_Id(Long id);
  boolean existsByCategory_Id(Long id);
}
