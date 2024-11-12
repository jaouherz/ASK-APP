package askapp.post.services;

import askapp.community.*;
import askapp.exeption.UserNotFoundException;
import askapp.post.Models.Post;
import askapp.post.Models.PostRequest;
import askapp.post.Models.ModelsINFO.PostINFO;
import askapp.post.blacklist.Blacklist;
import askapp.post.blacklist.BlacklistRepository;
import askapp.post.repositories.Postrepo;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BlacklistRepository blacklistRepository;
    private final Commemberrepo communityMemberrep;
    private final Postrepo postRepository;
    private final UserRepository userRepository;
    private final Commrepo commrepo;
    private final ComService communityserv;
    private final ComService comService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    public Post addPost(PostRequest request) throws Exception {
        User whoposted = userRepository.findById(request.getWhoposted())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Community community = commrepo.findById(request.getCommunity())
                .orElseThrow(() -> new  UserNotFoundException("Community not found"));
        String filteredContent = filterContent(request.getContent());
        Post post = Post.builder()
                .date_ajout(LocalDateTime.now())
                .whoposted(whoposted)
                .community(community)
                .isvisible(true)
                .content(filteredContent )
                .type(request.getType())
                .build();

        return postRepository.save(post);
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
                .map(this::mapToPostinfo)
                .collect(Collectors.toList());
    }

    private PostINFO mapToPostinfo(Post post) {
        return PostINFO.builder()
                .id(post.getId())
                .date_ajout(post.getDate_ajout())
                .whoposted(post.getWhoposted().getNom())
                .community(post.getCommunity().getTitle())
                .content(post.getContent())
                .type(post.getType())
                .likeList(likeService.getLikeByPost(post.getId()))
                .commentList(commentService.getCommentByPost(post.getId()))
                .build();
    }
    public PostINFO getPostInfoById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return mapToPostinfo(post);
    }
}
