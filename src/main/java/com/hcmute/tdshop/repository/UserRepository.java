package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  long countByEmail(String email);
  long countByPhone(String phone);
  long countByUsername(String username);
}
