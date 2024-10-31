package askapp.post.Models.ModelsINFO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeINFO {
    private long id;
    private String username;
    private long postid;
}
