package askapp.community.models;

import askapp.file.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRequest {


    private String title;
    private String description;
    private Long usercreate;
    private File image;
}
