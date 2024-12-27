package askapp.post.Report;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reprequest {
    private long whoreported;
    private String description;
    private String Cause;
    private long post;

}
