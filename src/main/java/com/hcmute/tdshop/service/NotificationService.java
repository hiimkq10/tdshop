package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.notification.AddNotificationRequest;
import com.hcmute.tdshop.dto.notification.UpdateNotificationRequest;
import com.hcmute.tdshop.model.DataResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

  DataResponse getAll(Pageable pageable);

  DataResponse search(Long id, LocalDateTime createdAt, Boolean sendAll, Pageable pageable);

  DataResponse insertNotification(AddNotificationRequest request);

  DataResponse updateNotification(long id, UpdateNotificationRequest request);

  DataResponse deleteNotification(long id);
}
