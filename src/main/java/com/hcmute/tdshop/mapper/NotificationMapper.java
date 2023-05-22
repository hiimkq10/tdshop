package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.notification.AddNotificationRequest;
import com.hcmute.tdshop.dto.notification.UpdateNotificationRequest;
import com.hcmute.tdshop.entity.Notification;
import com.hcmute.tdshop.entity.NotificationType;
import com.hcmute.tdshop.repository.NotificationTypeRepository;
import java.util.Optional;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {
  @Autowired
  NotificationTypeRepository notificationTypeRepository;

  public Notification AddNotificationRequestToNotification(AddNotificationRequest request) {
    if (request == null) {
      return null;
    }
    Notification notification = new Notification();
    notification.setTitle(request.getTitle());
    notification.setContent(request.getContent());
    if (request.getSendAll() != null) {
      notification.setSendAll(request.getSendAll());
    }
    notification.setUrl(request.getUrl());
    if (notification.getType() != null) {
      Optional<NotificationType> optionalType = notificationTypeRepository.findByIdAndDeletedAtIsNull(UUID.fromString(request.getTypeId()));
      optionalType.ifPresent(notification::setType);
    }

    return notification;
  }
  public abstract Notification UpdateNotificationRequestToNotification(UpdateNotificationRequest request);
}
