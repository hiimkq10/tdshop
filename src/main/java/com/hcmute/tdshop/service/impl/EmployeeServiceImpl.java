package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.user.AddEmployeeRequest;
import com.hcmute.tdshop.dto.user.EmployeeDto;
import com.hcmute.tdshop.dto.user.UpdateEmployeeRequest;
import com.hcmute.tdshop.entity.Employee;
import com.hcmute.tdshop.mapper.UserMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.EmployeeRepository;
import com.hcmute.tdshop.service.EmployeeService;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private Helper helper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public DataResponse getEmployee(Long id) {
    Optional<Employee> optionalEmployee = employeeRepository.findByIdAndUserInfo_DeletedAtIsNull(id);
    if (!optionalEmployee.isPresent()) {
      return new DataResponse(ApplicationConstants.NOT_FOUND, ApplicationConstants.USER_NOT_FOUND,
          ApplicationConstants.NOT_FOUND_CODE);
    }
    Employee employee = optionalEmployee.get();
    EmployeeDto employeeDto = userMapper.EmployeeToEmployeeDto(employee);
    return new DataResponse(employeeDto);
  }

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
      employee.getUserInfo().setPassword(encode(employee.getUserInfo().getPassword()));
      employee = employeeRepository.save(employee);
      return new DataResponse(ApplicationConstants.USER_ADD_SUCCESSFULLY, userMapper.EmployeeToEmployeeDto(employee));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_ADD_FAILED,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse updateEmployee(Long id, UpdateEmployeeRequest request) {
    Optional<Employee> optionalEmployee = employeeRepository.findByIdAndUserInfo_DeletedAtIsNull(id);
    if (optionalEmployee.isPresent()) {
      Employee currentEmployee = optionalEmployee.get();
      Employee employeeToUpdate = userMapper.UpdateEmployeeRequestToEmployee(request);
      if (Strings.isNotBlank(employeeToUpdate.getUserInfo().getFirstName())) {
        currentEmployee.getUserInfo().setFirstName(employeeToUpdate.getUserInfo().getFirstName());
      }
      if (Strings.isNotBlank(employeeToUpdate.getUserInfo().getLastName())) {
        currentEmployee.getUserInfo().setLastName(employeeToUpdate.getUserInfo().getLastName());
      }
      if (employeeToUpdate.getUserInfo().getBirthdate() != null) {
        currentEmployee.getUserInfo().setBirthdate(employeeToUpdate.getUserInfo().getBirthdate());
      }
      if (employeeToUpdate.getUserInfo().getGender() != null) {
        currentEmployee.getUserInfo().setGender(employeeToUpdate.getUserInfo().getGender());
      }
      if (employeeToUpdate.getUserInfo().getGender() != null) {
        currentEmployee.getUserInfo().setGender(employeeToUpdate.getUserInfo().getGender());
      }
      if (employeeToUpdate.getSalary() != null && employeeToUpdate.getSalary() > 0) {
        currentEmployee.setSalary(employeeToUpdate.getSalary());
      }
      return new DataResponse(userMapper.EmployeeToEmployeeDto(employeeRepository.saveAndFlush(currentEmployee)));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  private String encode(String str) {
    return passwordEncoder.encode(str);
  }
}
