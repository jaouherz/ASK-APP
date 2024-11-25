package askapp.user.usersrepo;

import askapp.user.models.Student;
import askapp.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface StudRepo extends JpaRepository<Student, Long>{
    Optional<User> findByEmail(String email);

}
