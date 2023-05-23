package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.Employee;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  Optional<Employee> findByIdAndUserInfo_DeletedAtIsNull(Long id);
}
