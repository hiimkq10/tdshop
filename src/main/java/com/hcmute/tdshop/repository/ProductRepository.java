package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.projection.product.ProductIdView;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
  Optional<Product> findByIdAndStatus_IdNotInAndDeletedAtNull(Long id, Long[] ids);
  boolean existsByIdAndStatus_IdNotInAndDeletedAtNull(Long id, Long[] ids);
  Set<Product> findByIdIn(Set<Long> ids);
  long countByBrand_Id(Long id);
  boolean existsBySetOfCategoriesContains(Category category);
//  boolean existsByAttribute_AttributeSet_Id(Long id);

  @Query(
      value = "SELECT DISTINCT id FROM product p "
          + "LEFT OUTER JOIN ( SELECT pm.product_id, pm.discount_rate  FROM product_promotion pm WHERE ?1 BETWEEN pm.start_date AND pm.end_date ) pm "
          + "ON p.id = pm.product_id "
          + "WHERE "
          + "ORDER BY pm.discount_rate ASC NULLS LAST",
      countQuery = "SELECT count(*) FROM product",
      nativeQuery = true
  )
  List<Product> getProductIdSortByPromotionASC(LocalDateTime dateTime, Pageable pageable);

  @Query(
      value = "SELECT DISTINCT id FROM product p "
          + "LEFT OUTER JOIN ( SELECT pm.product_id, pm.discount_rate  FROM product_promotion pm WHERE ?1 BETWEEN pm.start_date AND pm.end_date ) pm "
          + "ON p.id = pm.product_id "
          + "ORDER BY pm.discount_rate DESC NULLS LAST",
      countQuery = "SELECT count(*) FROM product",
      nativeQuery = true
  )
  List<Product> getProductIdSortByPromotionDESC(LocalDateTime dateTime, Pageable pageable);
}
