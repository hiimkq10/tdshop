package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.District;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
  List<District> findByProvince_Id(Long id, Sort sort);
  List<District> findByProvince_IdIn(List<Long> ids, Sort sort);
}
