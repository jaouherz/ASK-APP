package askapp.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Filerepo extends JpaRepository<File, String> {
}
