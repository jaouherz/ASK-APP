package askapp.community;

import askapp.user.User;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@SuperBuilder

public class CommunityINFO {
    private Long id;
    private String title;
    private String description;
    private long usercreate;
    private LocalDateTime createdatetime;
    private  boolean active;
}
