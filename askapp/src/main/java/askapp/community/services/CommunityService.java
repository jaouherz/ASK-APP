package askapp.community.services;

import askapp.auth.Userinfo;
import askapp.community.exceptions.UserAlreadyExistsException;
import askapp.community.models.Community;
import askapp.community.models.CommunityINFO;
import askapp.community.models.CommunityMember;
import askapp.community.models.CommunityRequest;
import askapp.community.repositories.CommunityMemberRepository;
import askapp.community.repositories.CommunityRepository;
import askapp.exeption.UserNotFoundException;
import askapp.user.models.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    @Autowired
    CommunityMemberRepository communityMemberRepository;
    public Community addCommunity(CommunityRequest request) throws Exception {
        Optional<Community> community = communityRepository.findByTitle(request.getTitle());

        if (community.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User createdby = userRepository.findById(request.getUsercreate())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Community com = Community.builder()
                .title(request.getTitle())
                .createdatetime(LocalDateTime.now())
                .usercreate(createdby)
                .description(request.getDescription())
                .active(true)
                .build();
        Community community1 = this.communityRepository.save(com);
        this.addMemberToCommunity(community1.getId(),request.getUsercreate());
        return community1;
    }




    public Community updateCommunity(Long id, CommunityRequest updatedCommunity) throws Exception {
        Community existingCommunity = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        if (updatedCommunity.getTitle() != null && !updatedCommunity.getTitle().equals(existingCommunity.getTitle())) {
            existingCommunity.setTitle(updatedCommunity.getTitle());
        }

        if (updatedCommunity.getDescription() != null && !updatedCommunity.getDescription().equals(existingCommunity.getDescription())) {
            existingCommunity.setDescription(updatedCommunity.getDescription());
        }



        return communityRepository.save(existingCommunity);
    }
    public void addMemberToCommunity(Long communityId, Long userId) throws Exception {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isAlreadyMember = community.getMembers() != null && community.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(userId));

        if (isAlreadyMember) {
            throw new Exception("User is already a member of the community");
        }

        CommunityMember newMember = new CommunityMember();
        newMember.setCommunity(community);
        newMember.setUser(user);
        newMember.setJoinedAt(LocalDateTime.now());

        if (community.getMembers() == null) {
            community.setMembers(new ArrayList<>());
        }

        community.getMembers().add(newMember);
        communityRepository.save(community);
    }
    public List<CommunityINFO> getRandomCommunityNotMember(long id ){
        List<Community> communities=this.communityRepository.findNotMemberCommunity(id);
        return communities.stream()
               .map(this::mapToCommunityINFO)
               .collect(Collectors.toList());
    }
    public CommunityINFO getBestSuggestion(long id){
        List<Community> communities=this.communityRepository.findNotMemberCommunity(id);
        int max=0;
        Community bestsuggestion=null;
        for(Community community : communities){
            if(community.getMembers().size()>max)
            {
                System.out.println(community.getTitle()+" : "+community.getMembers().size());
                max=community.getMembers().size();
                bestsuggestion=community;
            }
        }
        return this.mapToCommunityINFO(bestsuggestion);
    }
    public List<Userinfo> getCommunityMembers(long id){
        List<CommunityMember> members = communityMemberRepository.findByCommunityId(id);
        System.out.println(members.get(0).getCommunity().getId());
        List<User> users=new ArrayList<>();
        for (CommunityMember member:members){
            users.add(member.getUser());
        }
        return users.stream()
                .map(this::mapToUserINFO)
                .collect(Collectors.toList());
    }
    public CommunityINFO getCommunityById(long id){
        Community community=this.communityRepository.findById(id);

        return mapToCommunityINFO(community);
    }
    private CommunityINFO mapToCommunityINFO(Community community) {
        return CommunityINFO.builder()
                .id(community.getId())
                .title(community.getTitle())
                .description(community.getDescription())
                .usercreate(community.getUsercreate().getId())
                .createdatetime(community.getCreatedatetime())
                .active(community.isActive())
                .image(community.getImage())
                .build();
    }
    private Userinfo mapToUserINFO(User user) {
        return Userinfo.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .bio(user.getBio())
                .usernamez(user.getUsernamez())
                .isactive(user.isIsactive())
                .image(user.getImage())
                .build();
    }
}
