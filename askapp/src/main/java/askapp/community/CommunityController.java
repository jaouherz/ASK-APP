package askapp.community;

import askapp.auth.Userinfo;
import askapp.community.models.Community;
import askapp.community.models.CommunityINFO;
import askapp.community.models.CommunityRequest;
import askapp.community.repositories.CommunityMemberRepository;
import askapp.community.repositories.CommunityRepository;
import askapp.community.services.CommunityService;
import askapp.file.File;
import askapp.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/com")
@RequiredArgsConstructor

public class CommunityController {
    private final CommunityService servicecom;
    private final FileService Fileservice;
    private final CommunityRepository communityRepository;

    @PostMapping("/addcom")
    public ResponseEntity<Map<String, String>> add(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long usercreate,
            @RequestParam(required = false) MultipartFile image) {
        try {
            File pdp = Fileservice.saveAttachment(image);
            CommunityRequest request = CommunityRequest.builder()
                    .title(title)
                    .description(description)
                    .usercreate(usercreate)
                    .image(pdp)
                    .build();

            servicecom.addCommunity(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment successfully added");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
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
            File pdp = null;
            if (image != null) {
                pdp = Fileservice.saveAttachment(image);
            }
            CommunityRequest request = CommunityRequest.builder()
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
            List<Community> communities = communityRepository.findAll();
            return ResponseEntity.ok(communities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        try {
            communityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addMember")
    public ResponseEntity<String> addMemberToCommunity(@RequestParam Long communityId, @RequestParam Long userId
    ) {
        try {
            servicecom.addMemberToCommunity(communityId, userId);
            return ResponseEntity.ok("User added to community");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding user to community");
        }
    }
    @DeleteMapping("/deleteMember")
    public ResponseEntity<String> deleteMemberToCommunity(@RequestParam Long communityId, @RequestParam Long userId
    ) {
        try {
            servicecom.deleteMemeber(communityId, userId);
            return ResponseEntity.ok("User delete it successfully from community");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user from community :\n"+e.getMessage());
        }
    }
    @GetMapping("/members/{communityId}")
    public ResponseEntity<List<Userinfo>> getMembers(@PathVariable Long communityId) {
        try {
            return new ResponseEntity(this.servicecom.getCommunityMembers(communityId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/NotMembre/{id}")
    public ResponseEntity<List<CommunityINFO>> getNotMembers(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.servicecom.getRandomCommunityNotMember(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/community/{id}")
    public ResponseEntity<CommunityINFO> getCommunityById(@PathVariable Long id) {
        try {
            return new ResponseEntity<CommunityINFO>(servicecom.getCommunityById(id),HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("bestSuggestion/{id}")
    public ResponseEntity<CommunityINFO> BestSuggestion(@PathVariable Long id) {
        try {
            return new ResponseEntity<CommunityINFO>(this.servicecom.getBestSuggestion(id),HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("isMember/{userid}/{communityid}")
    public ResponseEntity<Boolean> isMember(@PathVariable(name = "communityid")long communityid,
                                            @PathVariable(name = "userid")long userid){
        try {
            return new ResponseEntity(this.servicecom.isMember(communityid,userid),HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("communityByUserId/{id}")
    public ResponseEntity<List<CommunityINFO>> communityByUserId(@PathVariable(name = "id") long id){
        try {
            return new ResponseEntity<List<CommunityINFO>>(this.servicecom.getCommunitiesByUser(id),HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
