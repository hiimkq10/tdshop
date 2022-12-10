package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProvinceRepository;
import com.hcmute.tdshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
  @RequestMapping("/user")
public class UserController {

  @Autowired
  private ProvinceRepository provinceRepository;

  @Autowired
  private UserService userService;

  @GetMapping("/my-info")
  public DataResponse getUserInfo() {
    return userService.getUserInfo();
  }

  @PostMapping("/ban/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public DataResponse banUser(@PathVariable(name = "id") long id) {
    return userService.banUser(id);
  }
}
