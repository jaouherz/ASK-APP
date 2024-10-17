package askapp.community;

import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Commrepo extends JpaRepository<Community, Long> {
}
