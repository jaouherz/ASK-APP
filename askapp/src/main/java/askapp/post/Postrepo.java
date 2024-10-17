package askapp.post;

import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Postrepo extends JpaRepository<User, Long> {
}
