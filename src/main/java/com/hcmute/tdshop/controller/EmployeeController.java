package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.user.AddEmployeeRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.EmployeeService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
  @Autowired
  EmployeeService employeeService;

  @PostMapping("/add")
  public DataResponse insertEmployee(@RequestBody @Valid AddEmployeeRequest request) {
    return employeeService.insertEmployee(request);
  }
}
