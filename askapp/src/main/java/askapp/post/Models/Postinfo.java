package askapp.post.Models;

import askapp.community.Community;
import askapp.post.typepost;
import askapp.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Postinfo {
    private Long id;
    private LocalDateTime date_ajout;
    private String whoposted ;
    private String community ;
    private String content ;
    private typepost type;
}
