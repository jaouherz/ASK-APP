package askapp.post.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
