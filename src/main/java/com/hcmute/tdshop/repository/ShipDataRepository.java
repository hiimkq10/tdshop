package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.ShipData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipDataRepository extends JpaRepository<ShipData, Long> {
  List<ShipData> findByOrder_Id(Long id);
  List<ShipData> findByOrder_IdAndDeletedAtIsNull(Long id);
}
