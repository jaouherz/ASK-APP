package askapp.post.Models.ModelsINFO;

import askapp.post.Models.typepost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostINFO {
    private Long id;
    private LocalDateTime date_ajout;
    private String whoposted ;
    private String community ;
    private String content ;
    private typepost type;
    private List<CommentINFO> commentList;
    private List<LikeINFO> likeList;
}
