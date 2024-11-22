package askapp.user.usersrepo;

import askapp.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);
    Optional<User> findByUsernamez(String Username);
    User findUserById(long id);
}
