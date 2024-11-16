package askapp.post.Models;

import askapp.community.models.Community;
import askapp.file.File;
import askapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date_ajout;
   @ManyToOne
    private User whoposted ;
    @ManyToOne
    private Community community ;
    private boolean isvisible ;
    private String content ;
    private typepost type;

    @OneToMany(mappedBy="post", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy="post",cascade = CascadeType.REMOVE)
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<File> images = new ArrayList<>();

    public void setImages(List<File> images) {
        this.images = images;
    }

    public List<File> getImages() {
        return images;
    }
}
