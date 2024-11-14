package askapp.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Commemberrepo extends JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findByCommunityId(Long communityId);
    List<CommunityMember> findByUserId(Long userId);
}
