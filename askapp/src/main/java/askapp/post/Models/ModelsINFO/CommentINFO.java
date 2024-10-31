package askapp.post.Models.ModelsINFO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentINFO {
    private long id;
    private String description;
    private Date date;
    private String username;
    private long postid;
}
