package askapp.user.usersrepo;

import askapp.post.Models.ModelsINFO.LikeINFO;
import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudRepo extends JpaRepository<LikeINFO.Student, Long>{
    Optional<User> findByEmail(String email);

}
