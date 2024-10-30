package askapp.post;

import askapp.community.Community;
import askapp.exeption.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/post")
@RestController

@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Postrepo postrepo;

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody PostRequest request) {
        try {

            Post post = postService.addPost(request);
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (UserNotFoundException  e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostRequest updatedPost) {
        try {
            Post post = postService.updatePost(postId, updatedPost);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/visibility/{postId}")
    public ResponseEntity<String> setPostVisibility(@PathVariable Long postId, @RequestParam boolean isVisible) {
        try {
            postService.setPostVisibility(postId, isVisible);
            return new ResponseEntity<>("Post visibility updated successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllposts() {
        try {
            List<Post> communities = postrepo.findAll();
            return ResponseEntity.ok(communities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
