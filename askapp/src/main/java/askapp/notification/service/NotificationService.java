package askapp.notification.service;

import askapp.auth.Userinfo;
import askapp.exeption.UserNotFoundException;
import askapp.notification.models.Notification;
import askapp.notification.models.NotificationINFO;
import askapp.notification.models.NotificationRequest;
import askapp.notification.repositories.NotificationRepository;
import askapp.post.Models.ModelsINFO.CommentINFO;
import askapp.post.Models.ModelsINFO.PostINFO;
import askapp.post.Models.Post;
import askapp.post.repositories.Postrepo;
import askapp.post.services.CommentService;
import askapp.post.services.LikeService;
import askapp.user.models.User;
import askapp.user.usersrepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    Postrepo postrepo;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserRepository userRepository;
    public List<NotificationINFO> getNotificationByUser(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Notification> notifications=this.notificationRepository.findNotificationsByUser(user);
        return notifications.stream()
                .sorted((notification1, notification2) -> notification2.getDate().compareTo(notification1.getDate()))
                .map(this::mapToNotificationInfo)
                .collect(Collectors.toList());
    }
    public Boolean addNotification(NotificationRequest notificationRequest){
        User whoreacted = userRepository.findById(notificationRequest.getWhoreacted())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Post post=this.postrepo.findById(notificationRequest.getPost())
        .orElseThrow(() -> new UserNotFoundException("Post not found"));
        if(notificationRequest.getWhoreacted()!=post.getWhoposted().getId()){
            Notification notification=new Notification();
            notification.setDate(new Date());
            notification.setPost(post);
            notification.setUser(post.getWhoposted());
            notification.setWhoreact(whoreacted);
            notification.setType(notificationRequest.getType());
            notification.setSeen(false);
            this.notificationRepository.save(notification);
            return true;
        }
        return false;
    }
    public NotificationINFO updateSeen(long id){
        Notification notification=this.notificationRepository.findById(id) ;
        notification.setSeen(true);
        return mapToNotificationInfo(this.notificationRepository.save(notification));
    }
    private NotificationINFO mapToNotificationInfo(Notification notification) {
        return NotificationINFO.builder()
                .id(notification.getId())
                .whoreact(this.mapToUserInfo(notification.getWhoreact()))
                .user(notification.getUser().getUsernamez())
                .type(notification.getType())
                .post(mapToPostinfo(notification.getPost()))
                .date(notification.getDate())
                .seen(notification.getSeen())
                .build();
    }

    private PostINFO mapToPostinfo(Post post) {
        return PostINFO.builder()
                .id(post.getId())
                .date_ajout(post.getDate_ajout())
                .whoposted(post.getWhoposted().getUsernamez())
                .community(post.getCommunity().getTitle())
                .communityID(post.getCommunity().getId())
                .content(post.getContent())
                .type(post.getType())
                .likeList(likeService.getLikeByPost(post.getId()))
                .commentList(
                        commentService.getCommentByPost(post.getId()).stream()
                                .sorted(Comparator.comparing(CommentINFO::getDate).reversed())
                                .collect(Collectors.toList())
                )                .fileList(post.getImages())
                .build();
    }
    private Userinfo mapToUserInfo(User user){
        return  Userinfo.builder()
                .id(user.getId())
                .usernamez(user.getUsernamez())
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .role(user.getRole())
                .image(user.getImage())
                .build();
    }

}
