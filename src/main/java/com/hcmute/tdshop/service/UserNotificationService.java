package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;

public interface UserNotificationService {
  DataResponse search(Long userId, Long notiId);
  DataResponse markAsRead(Long id);
  DataResponse deleteNoti(Long id);
}
