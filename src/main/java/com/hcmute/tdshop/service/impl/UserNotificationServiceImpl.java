package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.entity.Notification;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.entity.UserNotification;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.NotificationRepository;
import com.hcmute.tdshop.repository.UserNotificationRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.UserNotificationService;
import com.hcmute.tdshop.specification.NotificationSpecification;
import com.hcmute.tdshop.specification.UserNotificationSpecification;
import com.hcmute.tdshop.utils.SpecificationHelper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private UserNotificationRepository userNotificationRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public DataResponse search(Long userId, Long notiId, Pageable pageable) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      List<Notification> notifications = notificationRepository.findAll(NotificationSpecification.isSendAll(true));
      List<Specification<UserNotification>> specifications = new ArrayList<>();
//      specifications.add(UserNotificationSpecification.isNotDeleted());
      specifications.add(UserNotificationSpecification.hasUserId(userId));
      Specification<UserNotification> conditions = SpecificationHelper.and(specifications);
      List<UserNotification> userNotifications = userNotificationRepository.findAll(conditions);
      List<Long> notiIds = userNotifications.stream().map(n -> n.getNotification().getId())
          .collect(Collectors.toList());
      notifications.forEach(n -> {
        if (!notiIds.contains(n.getId())) {
          UserNotification userNotification = new UserNotification(null, false, false, user, n);
          userNotification = userNotificationRepository.save(userNotification);
//          n.getListOfUserNotifications().add(userNotification);
          userNotifications.add(userNotification);
        }
      });
      Collections.sort(userNotifications, new Comparator<UserNotification>() {
        @Override
        public int compare(UserNotification o1, UserNotification o2) {
          long sub = o2.getId() - o1.getId();
          if (sub > 0) {
            return 1;
          }
          if (sub < 0) {
            return -1;
          }
          return 0;
        }
      });
      return new DataResponse(
          userNotifications.stream().filter(item -> item.getIsDeleted() == null || !item.getIsDeleted()));
    }
    throw new RuntimeException(ApplicationConstants.USER_NOT_FOUND);
  }

  @Override
  public DataResponse markAsRead(Long id) {
    Optional<UserNotification> optionalNotification = userNotificationRepository.findById(id);
    if (optionalNotification.isPresent()) {
      UserNotification userNotification = optionalNotification.get();
      userNotification.setIsRead(true);
      userNotification = userNotificationRepository.save(userNotification);
      return new DataResponse(userNotification);
    }
    throw new RuntimeException(ApplicationConstants.NOTIFICATION_NOT_FOUND);
  }

  @Override
  public DataResponse deleteNoti(Long id) {
    Optional<UserNotification> optionalNotification = userNotificationRepository.findById(id);
    if (optionalNotification.isPresent()) {
      UserNotification userNotification = optionalNotification.get();
      userNotification.setIsDeleted(true);
      userNotification = userNotificationRepository.save(userNotification);
      return new DataResponse(userNotification);
    }
    throw new RuntimeException(ApplicationConstants.NOTIFICATION_NOT_FOUND);
  }
}
