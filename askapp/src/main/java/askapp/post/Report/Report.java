package askapp.post.Report;

import askapp.post.Models.Post;
import askapp.user.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@NoArgsConstructor
@Data
@SuperBuilder
@AllArgsConstructor
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Cause;
    private LocalDateTime date_ajout;
    private String etat;
    private String description;
   @ManyToOne
   @JoinColumn(name="post")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "whoreported")
    private User user;


}
