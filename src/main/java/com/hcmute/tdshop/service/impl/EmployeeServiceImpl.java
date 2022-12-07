package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.user.AddEmployeeRequest;
import com.hcmute.tdshop.entity.Employee;
import com.hcmute.tdshop.mapper.UserMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.EmployeeRepository;
import com.hcmute.tdshop.service.EmployeeService;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private Helper helper;

  @Override
  public DataResponse insertEmployee(AddEmployeeRequest request) {
    Employee employee = userMapper.AddEmployeeRequestToEmployee(request);
    if (employee != null) {
      if (helper.checkIfUsernameExisted(employee.getUserInfo().getUsername())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USERNAME_EXISTED,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      if (helper.checkIfEmailExisted(employee.getUserInfo().getEmail())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.EMAIL_EXISTED,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      if (helper.checkIfPhoneExisted(employee.getUserInfo().getPhone())) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PHONE_EXISTED,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
      employee = employeeRepository.save(employee);
      return new DataResponse(ApplicationConstants.SUCCESSFUL, ApplicationConstants.USER_ADD_SUCCESSFULLY, ApplicationConstants.SUCCESSFUL_CODE);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_ADD_FAILED, ApplicationConstants.BAD_REQUEST_CODE);
  }
}
