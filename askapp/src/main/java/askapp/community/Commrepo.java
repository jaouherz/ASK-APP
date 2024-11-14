package askapp.community;

import askapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Commrepo extends JpaRepository<Community, Long> {
   @Query(value = "SELECT * FROM community WHERE id NOT IN (SELECT community_id FROM community_member WHERE user_id = :id)", nativeQuery = true)
   Optional<Community> findNotMemberCommunity(@Param("id") long id);
   Optional< Community> findByTitle(String title);
}
