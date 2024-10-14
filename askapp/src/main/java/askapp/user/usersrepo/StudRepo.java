package askapp.user.usersrepo;

import askapp.user.Student;
import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudRepo extends JpaRepository<Student, Long>{
    Optional<User> findByEmail(String email);

}
