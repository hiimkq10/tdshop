package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUser_Id(Long id);
  List<Address> findByIsDefault(Boolean isDeafult);
}
