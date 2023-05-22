package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.dto.statistic.AccountStatisticDto;
import com.hcmute.tdshop.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Optional<User> findByEmail(String email);
  Optional<User> findByIdAndDeletedAtNullAndIsActiveTrue(Long id);
  boolean existsByIdAndDeletedAtNullAndIsActiveTrue(Long id);

  long countByEmail(String email);

  long countByPhone(String phone);

  long countByUsername(String username);

  Optional<User> findByUsername(String username);

  Optional<User> findByIdAndDeletedAtIsNull(long id);

  @Query(value = "SELECT "
      + "new com.hcmute.tdshop.dto.statistic.AccountStatisticDto(u.role.id as roleId, u.role.name as roleName, COUNT(u.id)) "
      + "FROM User u "
      + "WHERE u.deletedAt = NULL AND (:role = 0L OR u.role.id = :role) AND u.createdAt BETWEEN :startDate AND :endDate "
      + "GROUP BY u.role.id, u.role.name")
  List<AccountStatisticDto> accountStatistic(@Param("startDate") LocalDateTime start,
      @Param("endDate") LocalDateTime end, @Param("role") long role);
}
