package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByUser_Id(Long id);
}
