package com.hcmute.tdshop.service;

import com.hcmute.tdshop.model.DataResponse;
import org.springframework.data.domain.Pageable;

public interface UserNotificationService {
  DataResponse search(Long userId, Long notiId, Pageable pageable);
  DataResponse markAsRead(Long id);
  DataResponse deleteNoti(Long id);
}
