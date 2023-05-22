package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.NotificationType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, UUID> {
  Optional<NotificationType> findByIdAndDeletedAtIsNull(UUID id);
}
