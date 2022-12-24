package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUser_IdAndDeletedAtIsNull(Long id);
  List<Address> findByIsDefaultAndUser_IdAndDeletedAtIsNull(Boolean isDeafult, Long userId);
  boolean existsByIsDefaultAndUser_IdAndDeletedAtIsNull(Boolean isDeafult, Long userId);
  Optional<Address> findByIdAndUser_IdAndDeletedAtIsNull(Long id, Long userId);
  boolean existsByIdAndUser_Id(Long id, Long userId);
}
