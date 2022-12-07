package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.user.AddEmployeeRequest;
import com.hcmute.tdshop.dto.user.UpdateUserInfoRequest;
import com.hcmute.tdshop.dto.user.UserResponse;
import com.hcmute.tdshop.entity.Employee;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.enums.AccountRoleEnum;
import com.hcmute.tdshop.repository.AccountRoleRepository;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
  @Autowired
  AccountRoleRepository accountRoleRepository;

  public abstract UserResponse UserToUserResponse(User user);
  public abstract User UpdateUserInfoRequestToUser(UpdateUserInfoRequest request);

  public Employee AddEmployeeRequestToEmployee(AddEmployeeRequest request) {
    if (request == null) {
      return null;
    }
    Employee employee = new Employee();
    employee.setUserInfo(new User());
    employee.getUserInfo().setFirstName(request.getFirstName());
    employee.getUserInfo().setLastName(request.getLastName());
    employee.getUserInfo().setEmail(request.getEmail());
    employee.getUserInfo().setPhone(request.getPhone());
    employee.getUserInfo().setUsername(request.getUsername());
    employee.getUserInfo().setPassword(request.getPassword());
    employee.getUserInfo().setRole(accountRoleRepository.findById(AccountRoleEnum.ROLE_EMPLOYEE.getId()).get());
    employee.getUserInfo().setCreatedAt(LocalDateTime.now());
    employee.getUserInfo().setIsVerified(true);
    employee.getUserInfo().setIsActive(true);

    return employee;
  }
}
