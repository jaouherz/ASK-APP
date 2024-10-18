package askapp.community;

import askapp.exeption.UserNotFoundException;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
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
                .isactive(true)
                .build();

        return commrepo.save(com);
    }}
