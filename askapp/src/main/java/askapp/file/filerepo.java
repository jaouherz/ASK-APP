package askapp.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface filerepo extends JpaRepository<file, String> {
}
