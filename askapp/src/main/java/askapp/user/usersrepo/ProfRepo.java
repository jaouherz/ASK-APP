package askapp.user.usersrepo;

import askapp.user.Profesor;
import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfRepo  extends JpaRepository<Profesor, Long> {
    Optional<User> findByEmail(String email);

}
