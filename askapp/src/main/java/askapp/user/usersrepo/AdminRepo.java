package askapp.user.usersrepo;

import askapp.user.Admin;
import askapp.user.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo  extends JpaRepository<Admin, Long> {
}
