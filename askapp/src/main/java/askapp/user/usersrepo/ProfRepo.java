package askapp.user.usersrepo;

import askapp.user.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfRepo  extends JpaRepository<Profesor, Long> {
}
