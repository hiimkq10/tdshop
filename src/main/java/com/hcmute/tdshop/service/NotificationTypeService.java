package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.notificationtype.AddNotificationTypeDto;
import com.hcmute.tdshop.dto.notificationtype.UpdateNotificationTypeDto;
import com.hcmute.tdshop.model.DataResponse;

public interface NotificationTypeService {
  DataResponse getNotificationType(String id);
  DataResponse addNotificationType(AddNotificationTypeDto dto);
  DataResponse updateNotificationType(String id, UpdateNotificationTypeDto dto);
  DataResponse deleteNotificationType(String id);
}
