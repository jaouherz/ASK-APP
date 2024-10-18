package askapp.community;

import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Commrepo extends JpaRepository<Community, Long> {

   Optional< Community> findByTitle(String title);
}
