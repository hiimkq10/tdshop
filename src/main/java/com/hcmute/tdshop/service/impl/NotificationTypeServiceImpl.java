package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.notificationtype.AddNotificationTypeDto;
import com.hcmute.tdshop.dto.notificationtype.UpdateNotificationTypeDto;
import com.hcmute.tdshop.entity.NotificationType;
import com.hcmute.tdshop.mapper.NotificationTypeMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.NotificationTypeRepository;
import com.hcmute.tdshop.service.NotificationTypeService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

  @Autowired
  NotificationTypeRepository notificationTypeRepository;

  @Autowired
  NotificationTypeMapper notificationTypeMapper;

  @Override
  public DataResponse getNotificationType(String id) {
    Optional<NotificationType> optionalData = notificationTypeRepository.findByIdAndDeletedAtIsNull(
        UUID.fromString(id));
    return optionalData.map(DataResponse::new).orElseGet(
        () -> new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.NOTIFICATION_TYPE_NOT_FOUND,
            ApplicationConstants.BAD_REQUEST_CODE));
  }

  @Override
  public DataResponse addNotificationType(AddNotificationTypeDto dto) {
    NotificationType notificationType = notificationTypeMapper.AddNotificationTypeDtoToNotificationType(dto);
    notificationType.setCreatedAt(LocalDateTime.now());
    notificationType = notificationTypeRepository.save(notificationType);
    return new DataResponse(notificationType);
  }

  @Override
  public DataResponse updateNotificationType(String id, UpdateNotificationTypeDto dto) {
    NotificationType dataToUpdate = notificationTypeMapper.UpdateNotificationTypeDtoToNotificationType(dto);
    Optional<NotificationType> optionalData = notificationTypeRepository.findByIdAndDeletedAtIsNull(
        UUID.fromString(id));
    if (!optionalData.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.NOTIFICATION_TYPE_NOT_FOUND,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    NotificationType currentData = optionalData.get();
    if (Strings.isNotBlank(dataToUpdate.getName())) {
      currentData.setName(dataToUpdate.getName());
    }
    if (Strings.isNotBlank(dataToUpdate.getDescription())) {
      currentData.setDescription(dataToUpdate.getDescription());
    }
    if (Strings.isNotBlank(dataToUpdate.getTitleTemplate())) {
      currentData.setTitleTemplate(dataToUpdate.getTitleTemplate());
    }
    if (Strings.isNotBlank(dataToUpdate.getContentTemplate())) {
      currentData.setContentTemplate(dataToUpdate.getContentTemplate());
    }
    currentData = notificationTypeRepository.saveAndFlush(currentData);
    return new DataResponse(currentData);
  }

  @Override
  public DataResponse deleteNotificationType(String id) {
    Optional<NotificationType> optionalData = notificationTypeRepository.findByIdAndDeletedAtIsNull(
        UUID.fromString(id));
    if (!optionalData.isPresent()) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.NOTIFICATION_TYPE_NOT_FOUND,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    NotificationType data = optionalData.get();
    data.setDeletedAt(LocalDateTime.now());
    notificationTypeRepository.saveAndFlush(data);
    return new DataResponse(ApplicationConstants.NOTIFICATION_TYPE_DELETE_SUCCESS, true);
  }
}
