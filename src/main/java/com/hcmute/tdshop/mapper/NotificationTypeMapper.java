package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.notificationtype.AddNotificationTypeDto;
import com.hcmute.tdshop.dto.notificationtype.UpdateNotificationTypeDto;
import com.hcmute.tdshop.entity.NotificationType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class NotificationTypeMapper {

  public abstract NotificationType AddNotificationTypeDtoToNotificationType(AddNotificationTypeDto dto);

  public abstract NotificationType UpdateNotificationTypeDtoToNotificationType(UpdateNotificationTypeDto dto);
}
