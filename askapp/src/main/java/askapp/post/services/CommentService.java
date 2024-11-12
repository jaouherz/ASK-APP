package askapp.post.services;

import askapp.post.Models.Comment;
import askapp.post.Models.Like;
import askapp.post.Models.ModelsINFO.CommentINFO;
import askapp.post.Models.ModelsINFO.LikeINFO;
import askapp.post.Models.Post;
import askapp.post.repositories.CommentRepository;
import askapp.post.repositories.Postrepo;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    Postrepo postrepo;
    @Autowired
    UserRepository userRepository;
    public Comment addComment(Commentrequest commentRequest) throws Exception {
        Post post = postrepo.findById(commentRequest.getPost())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(commentRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .description(commentRequest.getDescription())
                .date(LocalDateTime.now())
                .username(user)
                .post(post)
                .build();

        post.getComments().add(comment);

        commentRepository.save(comment);
        postrepo.save(post);

        return comment;
    }
    public CommentINFO updateComment(Long commentId, Commentrequest updatedCommentRequest) throws Exception {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (updatedCommentRequest.getDescription() != null) {
            comment.setDescription(updatedCommentRequest.getDescription());
        }

        Comment updatedComment = commentRepository.save(comment);

        return mapToCommentinfo(updatedComment);
    }
    public void deleteComment(Long commentId) throws Exception {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Post post = comment.getPost();
        post.getComments().remove(comment);

        postrepo.save(post);
        commentRepository.delete(comment);
    }
    public List<CommentINFO> getCommentByPost(long postid){
        List<Comment> comments=commentRepository.findByPost(postrepo.findById(postid));

        return comments.stream()
                .map(this::mapToCommentinfo)
                .collect(Collectors.toList());
    }
    private CommentINFO mapToCommentinfo(Comment comment) {
        return CommentINFO.builder()
                .id(comment.getId())
                .username(comment.getUsername().getUsernamez())
                .description(comment.getDescription())
                .date(comment.getDate())
                .postid(comment.getPost().getId())
                .build();
    }


}
