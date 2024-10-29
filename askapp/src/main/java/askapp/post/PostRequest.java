package askapp.post;

import askapp.community.Community;
import askapp.user.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {



    private Long whoposted ;

    private Long community ;
    private String content ;
    private typepost type;
}
