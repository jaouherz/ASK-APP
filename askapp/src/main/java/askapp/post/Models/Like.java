package askapp.post.Models;

import askapp.user.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
     @ManyToOne
    @JoinColumn(name="user")
    private User username;
    @ManyToOne
    @JoinColumn(name="post")
    private Post post;


}
