package askapp.post.repositories;

import askapp.community.models.Community;
import askapp.file.File;
import askapp.post.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Postrepo extends JpaRepository<Post, Long> {
    Post findByImagesContaining(File file);
    List<Post> findByCommunity(Community community);
    List<Post> findByCommunityIn(List<Community> communities);
    List<Post> findByWhopostedId(long id);
}
