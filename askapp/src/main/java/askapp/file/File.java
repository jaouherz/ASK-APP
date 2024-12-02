package askapp.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class File {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public File(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public File(String id, String fileName, String fileType, byte[] data) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}

