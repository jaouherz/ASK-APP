package askapp.community;

import askapp.auth.RegisterRequest;
import askapp.auth.registerresponse;
import askapp.file.file;
import askapp.file.fileService;
import askapp.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/com")
@RequiredArgsConstructor

public class ComController {
    private final ComService servicecom;
    private final fileService Fileservice;
    private final Commrepo commrepo;
    private final Commemberrepo commemberrepo;

    @PostMapping("/addcom")
    public ResponseEntity<String> add(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long usercreate,
            @RequestParam(required = false) MultipartFile image){
   try {
        file pdp = Fileservice.saveAttachment(image);
        Comrequest request = Comrequest.builder()
                .title(title)
                .description(description)
                .usercreate(usercreate)
                .image(pdp)
                .build();
       Community saved = servicecom.addCommunity(request);
        return ResponseEntity.ok("Community "+saved.getTitle()+" has been saved");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    @PutMapping("/updatecom/{id}")
    public ResponseEntity<Community> updateCommunity(
            @PathVariable Long id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String title,

            @RequestParam(required = false) MultipartFile image) {
        try {
            file pdp = null;
            if (image != null) {
                pdp = Fileservice.saveAttachment(image);
            }
            Comrequest request = Comrequest.builder()
                    .title(title)
                    .description(description)

                    .image(pdp)
                    .build();
            Community saved = servicecom.updateCommunity(id, request);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/communities")
    public ResponseEntity<List<Community>> getAllCommunities() {
        try {
            List<Community> communities = commrepo.findAll();
            return ResponseEntity.ok(communities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/community/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable Long id) {
        try {
            Optional<Community> community = commrepo.findById(id);
            if (community.isPresent()) {
                return ResponseEntity.ok(community.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        try {
            commrepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addMember")
    public ResponseEntity<String> addMemberToCommunity(
            @RequestParam Long communityId,
            @RequestParam Long userId
    ) {
        try {
            servicecom.addMemberToCommunity(communityId, userId);
            return ResponseEntity.ok("User added to community");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding user to community");
        }
    }
    @GetMapping("/members/{communityId}")
    public ResponseEntity<List<CommunityMember>> getMembers(@PathVariable Long communityId) {
        try {
            List<CommunityMember> members = commemberrepo.findByCommunityId(communityId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/NotMembre/{id}")
    public ResponseEntity<Optional<Community>> getNotMembers(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(servicecom.getRandomCommunityNotMember(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
