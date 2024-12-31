package askapp.notification;

import askapp.notification.models.NotificationINFO;
import askapp.notification.models.NotificationRequest;
import askapp.notification.service.NotificationService;
import askapp.post.Models.Post;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/notification")
@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @PostMapping("addNotif")
    public ResponseEntity<String> addNotifcation(@RequestBody NotificationRequest notificationRequest){
        try {
            if(this.notificationService.addNotification(notificationRequest))
                return new ResponseEntity("Notification successfully saved", HttpStatus.OK);
            else
                return new ResponseEntity("Same Post creator can't make a notification", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("userNotifs/{id}")
    public ResponseEntity<List<NotificationINFO>> getUserNotifications(@PathVariable long id){
        try {
            return new ResponseEntity(notificationService.getNotificationByUser(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("seen/{id}")
    public ResponseEntity<NotificationINFO> addNotifcation(@PathVariable long id){
        try {
            return new ResponseEntity(this.notificationService.updateSeen(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    }
