package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.notification.AddNotificationRequest;
import com.hcmute.tdshop.dto.notification.UpdateNotificationRequest;
import com.hcmute.tdshop.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {
  public abstract Notification AddNotificationRequestToNotification(AddNotificationRequest request);
  public abstract Notification UpdateNotificationRequestToNotification(UpdateNotificationRequest request);
}
