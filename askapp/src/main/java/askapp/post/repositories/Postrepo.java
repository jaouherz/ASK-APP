package askapp.post.repositories;

import askapp.community.Community;
import askapp.post.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Postrepo extends JpaRepository<Post, Long> {

    List<Post> findByCommunityIn(List<Community> communities);
}
