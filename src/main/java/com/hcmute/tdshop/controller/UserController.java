package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.user.UpdateUserInfoRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/admin/get")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse getUsers(
      @RequestParam(name = "id", required = false, defaultValue = "0") long id,
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "role-id", required = false, defaultValue = "0") long roleId,
      Pageable pageable
  ) {
    return userService.getUsers(id, keyword, roleId, pageable);
  }

  @GetMapping("/my-info")
  public DataResponse getUserInfo() {
    return userService.getUserInfo();
  }

  @PutMapping("/update-info")
  public DataResponse updateUserInfo(@RequestBody @Valid UpdateUserInfoRequest request) {
    return userService.updateUserInfo(request);
  }

  @PostMapping("/admin/ban/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse banUser(@PathVariable(name = "id") long id) {
    return userService.banUser(id);
  }

  @PostMapping("/admin/unban/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse unBanUser(@PathVariable(name = "id") long id) {
    return userService.unBanUser(id);
  }
}
