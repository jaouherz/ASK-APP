package askapp.post.Models;

import askapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name="user")
    private User username;
    @ManyToOne
    @JoinColumn(name="post")
    private Post post;
}
