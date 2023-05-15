package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.notification.AddNotificationRequest;
import com.hcmute.tdshop.dto.notification.UpdateNotificationRequest;
import com.hcmute.tdshop.entity.Notification;
import com.hcmute.tdshop.mapper.NotificationMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.NotificationRepository;
import com.hcmute.tdshop.service.NotificationService;
import com.hcmute.tdshop.specification.NotificationSpecification;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private NotificationMapper notificationMapper;

  @Override
  public DataResponse getAll(Pageable pageable) {
    Page<Notification> notifications = notificationRepository.findAll(pageable);
    return new DataResponse(notifications);
  }

  @Override
  public DataResponse search(Long id, LocalDateTime createdAt, Boolean sendAll, Pageable pageable) {
    List<Specification<Notification>> specifications = new ArrayList<>();
    if (Objects.nonNull(id)) {
      specifications.add(NotificationSpecification.hasId(id));
    }
    if (Objects.nonNull(createdAt)) {
      specifications.add(NotificationSpecification.fromDate(createdAt));
    }
    if (Objects.nonNull(sendAll)) {
      specifications.add(NotificationSpecification.isSendAll(sendAll));
    }
    Specification<Notification> conditions = SpecificationHelper.and(specifications);
    Page<Notification> notifications = notificationRepository.findAll(conditions, pageable);
    return new DataResponse(notifications);
  }

  @Override
  public DataResponse insertNotification(AddNotificationRequest request) {
    Notification notification = notificationMapper.AddNotificationRequestToNotification(request);
    notification.setCreatedAt(LocalDateTime.now());
    notification = notificationRepository.save(notification);
    return new DataResponse(notification);
  }

  @Override
  public DataResponse updateNotification(long id, UpdateNotificationRequest request) {
    Optional<Notification> optionalNotification = notificationRepository.findById(id);
    if (optionalNotification.isPresent()) {
      Notification currentNotification = optionalNotification.get();
      Notification notificationToUpdate = notificationMapper.UpdateNotificationRequestToNotification(request);
      if (!Helper.checkIfStringIsBlank(notificationToUpdate.getContent())) {
        currentNotification.setContent(notificationToUpdate.getContent());
      }
      if (Objects.nonNull(notificationToUpdate.getSendAll())) {
        currentNotification.setSendAll(notificationToUpdate.getSendAll());
      }
      currentNotification = notificationRepository.save(currentNotification);
      return new DataResponse(currentNotification);
    }
    throw new RuntimeException(ApplicationConstants.NOTIFICATION_NOT_FOUND);
  }

  @Override
  public DataResponse deleteNotification(long id) {
    Optional<Notification> optionalNotification = notificationRepository.findById(id);
    if (optionalNotification.isPresent()) {
      notificationRepository.delete(optionalNotification.get());
      return DataResponse.SUCCESSFUL;
    }
    throw new RuntimeException(ApplicationConstants.NOTIFICATION_NOT_FOUND);
  }
}
