package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.UserClick;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserClickRepository extends JpaRepository<UserClick, Long> {
  List<UserClick> findFirst15ByUser_IdIsOrderByCreatedAtDesc(long id);
  List<UserClick> findByUser_IdIsAndCreatedAtBetween(long id, LocalDateTime minDate, LocalDateTime maxDate);
}
