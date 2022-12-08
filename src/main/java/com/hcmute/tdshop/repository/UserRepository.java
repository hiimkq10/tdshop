package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  long countByEmail(String email);
  long countByPhone(String phone);
  long countByUsername(String username);
  Optional<User> findByUsername(String username);
}
