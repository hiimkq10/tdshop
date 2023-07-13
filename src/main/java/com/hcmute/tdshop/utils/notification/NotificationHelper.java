package com.hcmute.tdshop.utils.notification;

import com.hcmute.tdshop.config.AppProperties;
import com.hcmute.tdshop.entity.Notification;
import com.hcmute.tdshop.entity.NotificationType;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.NotificationTypeRepository;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationHelper {

  @Autowired
  AppProperties appProperties;

  @Autowired
  NotificationTypeRepository notificationTypeRepository;

  public Notification buildNewProductAddedNotification(Product product) {
    Optional<NotificationType> optionalType = notificationTypeRepository.findByIdAndDeletedAtIsNull(
        UUID.fromString(appProperties.getNewProductAddedNotificationTypeId()));
    if (!optionalType.isPresent()) {
      new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.NOTIFICATION_TYPE_NOT_FOUND,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    NotificationType type = optionalType.get();
    Map<String, String> data = new HashMap<>();
    data.put("pro_name", product.getName());
    Notification notification = new Notification(
        null,
        replaceStringAll(type.getTitleTemplate(), data),
        replaceStringAll(type.getContentTemplate(), data),
        true,
        generateProductUrl(product.getId()),
        product.getImageUrl(),
        null,
        LocalDateTime.now(),
        type
    );
    return notification;
  }

  public Notification buildProductOutOfStockNotification(Product product) {
    Optional<NotificationType> optionalType = notificationTypeRepository.findByIdAndDeletedAtIsNull(
        UUID.fromString(appProperties.getProductOutOfStockNotificationTypeId()));
    if (!optionalType.isPresent()) {
      new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.NOTIFICATION_TYPE_NOT_FOUND,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    NotificationType type = optionalType.get();
    Map<String, String> data = new HashMap<>();
    data.put("pro_name", product.getName());
    Notification notification = new Notification(
        null,
        replaceStringAll(type.getTitleTemplate(), data),
        replaceStringAll(type.getContentTemplate(), data),
        false,
        generateProductUrl(product.getId()),
        product.getImageUrl(),
        null,
        LocalDateTime.now(),
        type
    );
    return notification;
  }

  public String replaceStringAll(String template, Map<String, String> data) {
    for (Map.Entry<String, String> entry : data.entrySet()) {
      template = template.replaceAll(Pattern.quote(String.format("${%s}", entry.getKey())), entry.getValue());
    }
    return template;
  }

  public String generateProductUrl(Long id) {
    return String.format("detail-product/%d", id);
  }
}
