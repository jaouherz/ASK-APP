package askapp.community.repositories;

import askapp.community.models.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findByCommunityId(Long communityId);
    List<CommunityMember> findByUserId(Long userId);
    CommunityMember findCommunityMemberByCommunityIdAndUserId(long communityID,long userID);
}
