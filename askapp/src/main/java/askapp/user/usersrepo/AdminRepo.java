package askapp.user.usersrepo;

import askapp.user.models.Admin;
import askapp.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminRepo  extends JpaRepository<Admin, Long> {
    Optional<User> findByEmail(String email);

}
