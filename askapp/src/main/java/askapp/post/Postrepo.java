package askapp.post;

import askapp.community.Community;
import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Postrepo extends JpaRepository<Post, Long> {

    List<Post> findByCommunityIn(List<Community> communities);
}
