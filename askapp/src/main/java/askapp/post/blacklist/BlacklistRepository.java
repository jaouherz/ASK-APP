package askapp.post.blacklist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    List<Blacklist> findAll();
}
