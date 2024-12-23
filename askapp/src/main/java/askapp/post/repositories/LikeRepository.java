package askapp.post.repositories;

import askapp.post.Models.Like;
import askapp.post.Models.Post;
import askapp.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    List<Like> findByPost(Optional<Post> post);
    Like findByUsernameAndAndPost(User user, Post post);

}
