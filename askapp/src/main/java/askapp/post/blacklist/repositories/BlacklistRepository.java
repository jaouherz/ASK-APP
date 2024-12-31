package askapp.post.blacklist.repositories;

import askapp.post.blacklist.models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    List<Blacklist> findAll();
    Optional<Blacklist> findBlacklistByWord(String word);
}
