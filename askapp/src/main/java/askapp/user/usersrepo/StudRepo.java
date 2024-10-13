package askapp.user.usersrepo;

import askapp.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudRepo extends JpaRepository<Student, Long>{
}
