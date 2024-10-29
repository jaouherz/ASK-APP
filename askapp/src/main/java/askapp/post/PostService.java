package askapp.post;

import askapp.community.Commrepo;
import askapp.community.Community;
import askapp.exeption.UserNotFoundException;
import askapp.post.blacklist.Blacklist;
import askapp.post.blacklist.BlacklistRepository;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final BlacklistRepository blacklistRepository;

    private final Postrepo postRepository;
    private final UserRepository userRepository;
    private final Commrepo commrepo;

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
}
