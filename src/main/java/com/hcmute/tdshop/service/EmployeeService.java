package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.user.AddEmployeeRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface EmployeeService {
  DataResponse insertEmployee(AddEmployeeRequest request);
}
