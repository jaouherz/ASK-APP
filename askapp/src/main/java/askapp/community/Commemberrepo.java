package askapp.community;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Commemberrepo extends JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findByCommunityId(Long communityId);
}
