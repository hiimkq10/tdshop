package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.dto.statistic.RatingDto;
import com.hcmute.tdshop.entity.Review;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
  @Query(value = "SELECT "
      + "new com.hcmute.tdshop.dto.statistic.RatingDto(p.id as id, p.name as name, AVG(r.ratingValue) as value, COUNT(p.id) as total) "
      + "FROM Review r INNER JOIN Product p ON r.product.id = p.id "
      + "WHERE r.isValid = true and r.createdAt BETWEEN :startDate AND :endDate "
      + "GROUP BY p.id, p.name "
      + "ORDER BY value DESC, total DESC")
  public List<RatingDto> ratingStatistic(@Param("startDate") LocalDateTime start,
      @Param("endDate") LocalDateTime end);

  @Query(value = "SELECT AVG(r.ratingValue) "
      + "FROM Review as r "
      + "WHERE isValid = true")
  public double ratingAvg();
}
