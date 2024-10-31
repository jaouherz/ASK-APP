package askapp.post.services;

import askapp.post.Models.Comment;
import askapp.post.Models.Like;
import askapp.post.Models.ModelsINFO.CommentINFO;
import askapp.post.Models.ModelsINFO.LikeINFO;
import askapp.post.Models.Post;
import askapp.post.repositories.CommentRepository;
import askapp.post.repositories.Postrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    Postrepo postrepo;
    public Comment addComment(Comment comment){

        Post post = postrepo.findById(comment.getPost().getId()).orElseThrow(() -> new RuntimeException("Post not found"));

        post.getComments().add(comment);

        commentRepository.save(comment);

        postrepo.save(post);

        return commentRepository.save(comment);

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
