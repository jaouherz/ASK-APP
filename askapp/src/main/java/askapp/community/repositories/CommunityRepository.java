package askapp.community.repositories;

import askapp.community.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
   @Query(value = "SELECT * FROM community WHERE id NOT IN (SELECT community_id FROM community_member WHERE user_id = :id )", nativeQuery = true)
   List<Community> findNotMemberCommunity(@Param("id") long id);
   Optional< Community> findByTitle(String title);
   Community findById(long id);
   @Query(value = "SELECT * FROM community WHERE id  IN (SELECT community_id FROM community_member WHERE user_id = :id )", nativeQuery = true)
   List<Community> getCommunityByUserId(@Param("id")long id);

}
