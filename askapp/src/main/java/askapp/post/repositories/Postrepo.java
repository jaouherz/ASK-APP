package askapp.post.repositories;

import askapp.community.models.Community;
import askapp.post.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Postrepo extends JpaRepository<Post, Long> {
    List<Post> findByCommunity(Community community);

    List<Post> findByCommunityIn(List<Community> communities);
}
