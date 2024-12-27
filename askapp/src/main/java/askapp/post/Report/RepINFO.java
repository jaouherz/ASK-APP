package askapp.post.Report;

import askapp.post.Models.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepINFO {
    private Long id;
    private String cause;
    private LocalDateTime date_ajout;
    private String whoposted;
    private Long post;
    private String etat;
    private String description;
    private String whoreported;
}
