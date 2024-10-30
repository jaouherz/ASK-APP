package askapp.post.Models;

import askapp.community.Community;
import askapp.post.typepost;
import askapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
}
