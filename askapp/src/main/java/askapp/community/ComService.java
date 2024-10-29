package askapp.community;

import askapp.exeption.UserNotFoundException;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComService {
public final Commrepo commrepo;
    private final UserRepository userRepository;

    public Community addCommunity(Comrequest request) throws Exception {
        Optional<Community> community = commrepo.findByTitle(request.getTitle());

        if (community.isPresent()) {
            throw new UserAlreadyExistsException(); // Rename your exception class appropriately
        }

        User createdby = userRepository.findById(request.getUsercreate())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Community com = Community.builder()
                .title(request.getTitle())
                .createdatetime(LocalDateTime.now())
                .usercreate(createdby)
                .active(true)
                .build();

        return commrepo.save(com);
    }




    public Community updateCommunity(Long id, Comrequest updatedCommunity) throws Exception {
        Community existingCommunity = commrepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        if (updatedCommunity.getTitle() != null && !updatedCommunity.getTitle().equals(existingCommunity.getTitle())) {
            existingCommunity.setTitle(updatedCommunity.getTitle());
        }

        if (updatedCommunity.getDescription() != null && !updatedCommunity.getDescription().equals(existingCommunity.getDescription())) {
            existingCommunity.setDescription(updatedCommunity.getDescription());
        }



        return commrepo.save(existingCommunity);
    }
    public void addMemberToCommunity(Long communityId, Long userId) throws Exception {
        Community community = commrepo.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isAlreadyMember = community.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(userId));

        if (isAlreadyMember) {
            throw new Exception("User is already a member of the community");
        }

        CommunityMember newMember = new CommunityMember();
        newMember.setCommunity(community);
        newMember.setUser(user);
        newMember.setJoinedAt(LocalDateTime.now());

        community.getMembers().add(newMember);
        commrepo.save(community);
    }
}
