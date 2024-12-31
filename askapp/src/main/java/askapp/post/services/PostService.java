package askapp.post.services;

import askapp.community.models.Community;
import askapp.community.models.CommunityMember;
import askapp.community.repositories.CommunityMemberRepository;
import askapp.community.repositories.CommunityRepository;
import askapp.community.services.CommunityService;
import askapp.exeption.UserNotFoundException;
import askapp.file.File;
import askapp.file.FileService;
import askapp.post.Models.ModelsINFO.CommentINFO;
import askapp.post.Models.Post;
import askapp.post.Models.PostRequest;
import askapp.post.Models.ModelsINFO.PostINFO;
import askapp.post.blacklist.models.Blacklist;
import askapp.post.blacklist.repositories.BlacklistRepository;
import askapp.post.repositories.Postrepo;
import askapp.user.models.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService {

    private final BlacklistRepository blacklistRepository;
    private final CommunityMemberRepository communityMemberrep;
    private final Postrepo postRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommunityService communityserv;
    private final CommunityService communityService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    FileService fileservice;

    public PostINFO addPost(PostRequest request) throws Exception {
        User whoposted = userRepository.findById(request.getWhoposted())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Community community = communityRepository.findById(request.getCommunity())
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        String filteredContent = filterContent(request.getContent());

        Post post = Post.builder()
                .date_ajout(LocalDateTime.now())
                .whoposted(whoposted)
                .community(community)
                .isvisible(true)
                .content(filteredContent)
                .type(request.getType())
                .build();

        List<MultipartFile> images = request.getImages();
        if (images != null && !images.isEmpty()) {
            List<File> savedImages = new ArrayList<>();
            for (MultipartFile image : images) {
                File savedImage = fileservice.saveAttachment(image);
                savedImages.add(savedImage);
            }
            post.setImages(savedImages);
        }

        return mapToPostinfo(postRepository.save(post));
    }
    public Post updatePost(Long postId, PostRequest updatedPost) throws Exception {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (updatedPost.getContent() != null && !updatedPost.getContent().equals(existingPost.getContent())) {
            existingPost.setContent(updatedPost.getContent());
        }



        return postRepository.save(existingPost);
    }

    public void setPostVisibility(Long postId, boolean isVisible) throws EntityNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        post.setIsvisible(isVisible);
        postRepository.save(post);
    }

    private String filterContent(String content) {
        List<Blacklist> blacklistWords = blacklistRepository.findAll();
        for (Blacklist blacklistWord : blacklistWords) {
            content = content.replaceAll("(?i)\\b" + blacklistWord.getWord() + "\\b", "***");
        }
        return content;
    }
    public List<PostINFO> getPostsByUserId(long userId) {
        List<Community> communities = communityMemberrep.findByUserId(userId)
                .stream()
                .map(CommunityMember::getCommunity)
                .collect(Collectors.toList());

        List<Post> posts = postRepository.findByCommunityIn(communities);

        return posts.stream()
                .sorted((post1, post2) -> post2.getDate_ajout().compareTo(post1.getDate_ajout()))
                .map(this::mapToPostinfo)
                .collect(Collectors.toList());
    }

    private PostINFO mapToPostinfo(Post post) {
        return PostINFO.builder()
                .id(post.getId())
                .date_ajout(post.getDate_ajout())
                .whoposted(post.getWhoposted().getUsernamez())
                .community(post.getCommunity().getTitle())
                .communityID(post.getCommunity().getId())
                .content(post.getContent())
                .type(post.getType())
                .likeList(likeService.getLikeByPost(post.getId()))
                .commentList(
                        commentService.getCommentByPost(post.getId()).stream()
                                .sorted(Comparator.comparing(CommentINFO::getDate).reversed())
                                .collect(Collectors.toList())
                )                .fileList(post.getImages())
                .build();
    }
    public PostINFO getPostInfoById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return mapToPostinfo(post);
    }
    public List<PostINFO> getPostsByCommunityId(long community_id){
        Community community=communityRepository.findById(community_id);
        List<Post> posts=postRepository.findByCommunity(community);
        return posts.stream()
                .sorted((post1, post2) -> post2.getDate_ajout().compareTo(post1.getDate_ajout()))
                .map(this::mapToPostinfo)
                .collect(Collectors.toList());
    }
    public PostINFO getPostByFileImage(String fileID) throws Exception {
        File file=fileservice.getAttachment(fileID);
        Post post=postRepository.findByImagesContaining(file);
        return this.mapToPostinfo(post);
    }
    public Map<String, Long> getPostCountsByCommunity() {
        // Get all posts
        List<Post> posts = postRepository.findAll();

        // Create a map to store community names and their corresponding post counts
        Map<String, Long> communityPostCounts = new HashMap<>();

        // Iterate over posts to count posts for each community
        for (Post post : posts) {
            String communityName = post.getCommunity().getTitle();
            communityPostCounts.put(communityName, communityPostCounts.getOrDefault(communityName, 0L) + 1);
        }

        return communityPostCounts;
    }
    public Map<String, Long> getPostCountsByUser() {
        List<Post> posts = postRepository.findAll();  // Fetch all posts from the database

        Map<String, Long> userPostCounts = posts.stream()
                .collect(Collectors.groupingBy(post -> post.getWhoposted().getUsernamez(), Collectors.counting()));

        return userPostCounts.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(10)  // Get the top 10 users
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));  // Maintain insertion order
    }
    public Map<Integer, Long> getPostCountsByYear() {
        List<Post> posts = postRepository.findAll();

        // Group posts by year and count
        return posts.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getDate_ajout().getYear(), // Group by year
                        Collectors.counting() // Count posts per year
                ));
    }

    public Map<String, Long> getPostCountsByYearAndMonth() {
        List<Post> posts = postRepository.findAll();

        // Group posts by year and month and count
        return posts.stream()
                .collect(Collectors.groupingBy(
                        post -> {
                            LocalDateTime date = post.getDate_ajout();
                            return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
                        },
                        Collectors.counting()
                ));
    }
    public List<PostINFO> getUserPosts(long id){
        List<Post> posts=this.postRepository.findByWhopostedId(id);
        return posts.stream()
                .sorted((post1, post2) -> post2.getDate_ajout().compareTo(post1.getDate_ajout()))
                .map(this::mapToPostinfo)
                .collect(Collectors.toList());
    }
}
