package askapp.post;

import askapp.community.Commrepo;
import askapp.community.Community;
import askapp.exeption.UserNotFoundException;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {


    private final Postrepo postRepository;
    private final UserRepository userRepository;
    private final Commrepo commrepo;

    public Post addPost(PostRequest request) throws Exception {
        User whoposted = userRepository.findById(request.getWhoposted().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Community community = commrepo.findById(request.getCommunity().getId())
                .orElseThrow(() -> new  UserNotFoundException("Community not found"));

        Post post = Post.builder()
                .date_ajout(LocalDateTime.now())
                .whoposted(whoposted)
                .community(community)
                .isvisible(true)
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


}
