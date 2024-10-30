package askapp.post.Models;

import askapp.community.Community;
import askapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date_ajout;
   @ManyToOne
    private User whoposted ;
    @ManyToOne
    private Community community ;
    private boolean isvisible ;
    private String content ;
    private typepost type;

    @OneToMany(mappedBy="post", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy="post",cascade = CascadeType.REMOVE)
    private List<Comment> comments;
}
