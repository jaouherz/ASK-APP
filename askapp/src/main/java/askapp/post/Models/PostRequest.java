package askapp.post.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private Long whoposted ;
    private Long community ;
    private String content ;
    private typepost type;
    private List<MultipartFile> images;
}
