package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Wards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardsRepository extends JpaRepository<Wards, Long> {

}
