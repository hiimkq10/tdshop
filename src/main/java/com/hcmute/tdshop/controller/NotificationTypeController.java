package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.notificationtype.AddNotificationTypeDto;
import com.hcmute.tdshop.dto.notificationtype.UpdateNotificationTypeDto;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.NotificationTypeService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noti-type")
public class NotificationTypeController {

  @Autowired
  NotificationTypeService notificationTypeService;

  @GetMapping("/{id}")
  public DataResponse get(@PathVariable(name = "id", value = "") String id) {
    return notificationTypeService.getNotificationType(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertNotificationType(@RequestBody @Valid AddNotificationTypeDto dto) {
    return notificationTypeService.addNotificationType(dto);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updateNotificationType(@PathVariable(name = "id", value = "") String id,
      @RequestBody @Valid UpdateNotificationTypeDto dto) {
    return notificationTypeService.updateNotificationType(id, dto);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deleteNotificationType(@PathVariable(name = "id", value = "") String id) {
    return notificationTypeService.deleteNotificationType(id);
  }
}
