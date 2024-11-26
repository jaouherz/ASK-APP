package askapp.community.models;

import askapp.file.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityINFO {
    private Long id;
    private String title;
    private String description;
    private long usercreate;
    private LocalDateTime createdatetime;
    private  boolean active;
    private File image;
}
