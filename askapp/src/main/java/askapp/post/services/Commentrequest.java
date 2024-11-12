package askapp.post.services;

import askapp.post.Models.Post;
import askapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Commentrequest {


    private String description;
    private LocalDateTime date;

    private Long username;

    private Long post;
}
