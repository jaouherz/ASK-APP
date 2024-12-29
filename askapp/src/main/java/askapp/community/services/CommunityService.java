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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {
    @Autowired
    private  CommunityRepository communityRepository;
    @Autowired
    private  UserRepository userRepository;
    private final LevenshteinDistance levenshteinDistance;
    public CommunityService(){
        this.levenshteinDistance = new LevenshteinDistance();
    }
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
    public List<CommunityINFO> getAll(){
        List<Community> communities=communityRepository.findAll();
        return communities.stream()
                .map(this::mapToCommunityINFO)
                .collect(Collectors.toList());
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
    @Transactional
    public void deleteMemeber(Long communityId, Long userId) throws Exception {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        System.out.println("test1 ");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        System.out.println("test0");

        CommunityMember communityMember=communityMemberRepository.findCommunityMemberByCommunityIdAndUserId(community.getId(),user.getId());
        if (communityMember==null) {
            throw new Exception("User is not a member in the community");
        }
        System.out.println("test");
        communityMemberRepository.delete(communityMember);
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
    public List<CommunityINFO> getCommunitiesByUser(long userId){
        List<Community> communities=this.communityRepository.getCommunityByUserId(userId);
        return communities.stream()
                .map(this::mapToCommunityINFO)
                .collect(Collectors.toList());
    }
    public List<CommunityINFO> getCommunitySearch(String search) {
        List<Community> communities = communityRepository.findAll();
        Map<Community, Integer> similarityMap = new HashMap<>();
        for (Community community : communities) {
            int distance = levenshteinDistance.apply(search, community.getTitle());
            similarityMap.put(community, distance);
        }

        List<Community> fuzzyResults = new ArrayList<>(similarityMap.keySet());
        fuzzyResults.sort(Comparator.comparingInt(similarityMap::get));

        return fuzzyResults.stream()
                .map(this::mapToCommunityINFO)
                .collect(Collectors.toList());
    }
    public Boolean isMember(long idCommunity,long iduser){
        CommunityMember communityMember=this.communityMemberRepository.findCommunityMemberByCommunityIdAndUserId(idCommunity,iduser);
        if(communityMember!=null)
            return true ;
        else
            return false;
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

