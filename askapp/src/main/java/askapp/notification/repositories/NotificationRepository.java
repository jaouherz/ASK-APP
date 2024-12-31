package askapp.notification.repositories;

import askapp.notification.models.Notification;
import askapp.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findNotificationsByUser(User user);
    Notification findById(long id);
}
