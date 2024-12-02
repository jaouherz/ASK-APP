package askapp.post.Models.ModelsINFO;

import askapp.user.models.User;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeINFO {
    private long id;
    private String username;
    private long postid;

    @Entity
    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student extends User {
    private String classse ;
    }
}
