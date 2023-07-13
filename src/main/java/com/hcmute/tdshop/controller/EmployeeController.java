package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.user.AddEmployeeRequest;
import com.hcmute.tdshop.dto.user.UpdateEmployeeRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.EmployeeService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired
  EmployeeService employeeService;

  @GetMapping("/get/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public DataResponse getEmployees(
      @PathVariable(name = "id") Long id,
      Pageable pageable
  ) {
    return employeeService.getEmployee(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public DataResponse insertEmployee(@RequestBody @Valid AddEmployeeRequest request) {
    return employeeService.insertEmployee(request);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public DataResponse updateUserInfo(@PathVariable(name = "id") Long id,
      @RequestBody @Valid UpdateEmployeeRequest request) {
    return employeeService.updateEmployee(id, request);
  }
}
