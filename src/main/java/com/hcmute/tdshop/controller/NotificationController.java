package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.mastercategory.AddMasterCategoryRequest;
import com.hcmute.tdshop.dto.mastercategory.UpdateMasterCategoryRequest;
import com.hcmute.tdshop.dto.notification.AddNotificationRequest;
import com.hcmute.tdshop.dto.notification.UpdateNotificationRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.NotificationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

  @Autowired
  NotificationService notificationService;

  @GetMapping("/get-all")
  public DataResponse getAll(Pageable pageable) {
    return notificationService.getAll(pageable);
  }

  @GetMapping("/search")
  public DataResponse search(
      @RequestParam(name = "id", required = false) Long id,
      @RequestParam(name = "from-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime fromDate,
      @RequestParam(name = "send-all", required = false) Boolean sendAll,
      Pageable pageable) {
    return notificationService.search(id, fromDate, sendAll, pageable);
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse insertNotification(@RequestBody @Valid AddNotificationRequest request) {
    return notificationService.insertNotification(request);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse updateNotification(@PathVariable(name = "id") long id,
      @RequestBody @Valid UpdateNotificationRequest request) {
    return notificationService.updateNotification(id, request);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
  public DataResponse deleteNotification(@PathVariable(name = "id") long id) {
    return notificationService.deleteNotification(id);
  }
}
