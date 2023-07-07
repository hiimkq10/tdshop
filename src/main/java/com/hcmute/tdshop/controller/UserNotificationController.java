package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-noti")
public class UserNotificationController {

  @Autowired
  private UserNotificationService userNotificationService;

  @GetMapping("/search")
  public DataResponse search(
      @RequestParam(name = "user-id", required = false) Long userId,
      @RequestParam(name = "noti-id", required = false) Long notiId,
      Pageable pageable) {
    return userNotificationService.search(userId, notiId, pageable);
  }

  @PostMapping("/mark-as-read/{id}")
  public DataResponse markAsRead(@PathVariable(name = "id") Long id) {
    return userNotificationService.markAsRead(id);
  }

  @DeleteMapping("/delete/{id}")
  public DataResponse deleteNoti(@PathVariable(name = "id") Long id) {
    return userNotificationService.deleteNoti(id);
  }
}
