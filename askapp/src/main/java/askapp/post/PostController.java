package askapp.post;

import askapp.exeption.UserNotFoundException;
import askapp.file.File;
import askapp.post.Models.Comment;
import askapp.post.Models.ModelsINFO.CommentINFO;
import askapp.post.Models.ModelsINFO.LikeINFO;
import askapp.post.Models.ModelsINFO.PostINFO;
import askapp.post.Models.Post;
import askapp.post.Models.PostRequest;
import askapp.post.Models.typepost;
import askapp.post.repositories.Postrepo;
import askapp.post.services.CommentService;
import askapp.post.services.Commentrequest;
import askapp.post.services.LikeService;
import askapp.post.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/post")
@RestController

@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Postrepo postrepo;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;

    @PostMapping("/addpost")
    public ResponseEntity<PostINFO> addPost(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long whoposted,
            @RequestParam(required = false) Long community,
            @RequestParam(required = false) typepost type,
            @RequestParam(required = false) List<MultipartFile> images) {

        try {
            PostRequest postRequest = PostRequest.builder()
                    .content(content)
                    .whoposted(whoposted)
                    .community(community)
                    .type(type)
                    .images(images)
                    .build();

            PostINFO savedPost = postService.addPost(postRequest);

            return ResponseEntity.ok(savedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<List<PostINFO>> getPostsByUserId(@PathVariable long userId) {
        List<PostINFO> posts = postService.getPostsByUserId(userId);
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
    @GetMapping("/{postId}/info")
    public ResponseEntity<PostINFO> getPostInfoById(@PathVariable Long postId) {
        try {
            PostINFO postInfo = postService.getPostInfoById(postId);
            return new ResponseEntity<>(postInfo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/addcomment")
    public ResponseEntity<CommentINFO> createPost(@RequestBody Commentrequest request) {
        try {
            return new ResponseEntity<CommentINFO>(commentService.addComment(request), HttpStatus.CREATED);
        } catch (UserNotFoundException  e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updatecomment/{commentId}")
    public ResponseEntity<CommentINFO> updateComment(@PathVariable Long commentId, @RequestBody Commentrequest updatedRequest) {
        try {
            CommentINFO updatedComment = commentService.updateComment(commentId, updatedRequest);
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletecomment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>("Comment successfully deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Comment not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/pressLike")
    public ResponseEntity<LikeINFO> pressLike(@RequestBody LikeINFO likeINFO){
        try {
            return new ResponseEntity<LikeINFO>(this.likeService.pressLike(likeINFO.getPostid(),likeINFO.getUserID()),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity( e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentINFO>> getCommentsByPost(@PathVariable long postId) {
        try {
            List<CommentINFO> comments = commentService.getCommentByPost(postId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/community/{id}")
    public ResponseEntity<List<PostINFO>> getPostByCommunityId(@PathVariable("id") long communityID){
        try {
            return new ResponseEntity<List<PostINFO>>(this.postService.getPostsByCommunityId(communityID), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/postByImage/{id}")
    public ResponseEntity<PostINFO>getPostByImage(@PathVariable String id){
        try{
            return new ResponseEntity<PostINFO>(this.postService.getPostByFileImage(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/counts")
    public Map<String, Long> getCommunityPostCounts() {
        return postService.getPostCountsByCommunity();
    }
    @GetMapping("/top-users")
    public Map<String, Long> getTopUsers() {
        return postService.getPostCountsByUser();
    }
    @GetMapping("/post-counts-by-year")
    public ResponseEntity<Map<Integer, Long>> getPostCountsByYear() {
        Map<Integer, Long> counts = postService.getPostCountsByYear();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/post-counts-by-year-and-month")
    public ResponseEntity<Map<String, Long>> getPostCountsByYearAndMonth() {
        Map<String, Long> counts = postService.getPostCountsByYearAndMonth();
        return ResponseEntity.ok(counts);
    }
}
