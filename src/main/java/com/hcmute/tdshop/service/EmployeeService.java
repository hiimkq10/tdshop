package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.user.AddEmployeeRequest;
import com.hcmute.tdshop.dto.user.UpdateEmployeeRequest;
import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
  DataResponse getEmployee(Long id);
  DataResponse insertEmployee(AddEmployeeRequest request);
  DataResponse updateEmployee(Long id, UpdateEmployeeRequest request);
}
