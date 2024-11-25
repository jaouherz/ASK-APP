package askapp.file;

import org.springframework.web.multipart.MultipartFile;

public interface Fileservice1 {
    File saveAttachment(MultipartFile file) throws Exception;

    File getAttachment(String fileId) throws Exception;

}
