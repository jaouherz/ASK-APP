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




    public Community updateCommunity(Community updatedCommunity) throws Exception {
        Community existingCommunity = commrepo.findById(updatedCommunity.getId())
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        if (updatedCommunity.getTitle() != null && !updatedCommunity.getTitle().equals(existingCommunity.getTitle())) {
            existingCommunity.setTitle(updatedCommunity.getTitle());
        }

        if (updatedCommunity.getDescription() != null && !updatedCommunity.getDescription().equals(existingCommunity.getDescription())) {
            existingCommunity.setDescription(updatedCommunity.getDescription());
        }



        return commrepo.save(existingCommunity);
    }
}
