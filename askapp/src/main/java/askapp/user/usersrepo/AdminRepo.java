package askapp.user.usersrepo;

import askapp.user.Admin;
import askapp.user.Profesor;
import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo  extends JpaRepository<Admin, Long> {
    Optional<User> findByEmail(String email);

}
