package askapp.notification.models;

import askapp.post.Models.Post;
import askapp.user.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;

    @ManyToOne
    private Post post;
    @ManyToOne
    private User whoreact;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Boolean seen;

}
