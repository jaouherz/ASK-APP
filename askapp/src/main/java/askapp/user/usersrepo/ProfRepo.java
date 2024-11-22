package askapp.user.usersrepo;

import askapp.user.models.Profesor;
import askapp.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface ProfRepo  extends JpaRepository<Profesor, Long> {
    Optional<User> findByEmail(String email);

}
