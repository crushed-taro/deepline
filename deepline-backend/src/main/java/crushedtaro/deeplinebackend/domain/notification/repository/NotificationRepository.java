package crushedtaro.deeplinebackend.domain.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.member.entity.Member;
import crushedtaro.deeplinebackend.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findAllByReceiverAndIsReadFalseOrderByCreatedAtDesc(Member receiver);
}
