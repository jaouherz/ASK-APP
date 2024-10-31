package askapp.post.services;

import askapp.post.Models.Like;
import askapp.post.Models.ModelsINFO.LikeINFO;
import askapp.post.Models.ModelsINFO.PostINFO;
import askapp.post.Models.Post;
import askapp.post.repositories.LikeRepository;
import askapp.post.repositories.Postrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    Postrepo postrepo;
    public Like addLike(Like like){

        Post post = postrepo.findById(like.getPost().getId()).orElseThrow(() -> new RuntimeException("Post not found"));

        post.getLikes().add(like);

        likeRepository.save(like);

        postrepo.save(post);

        return likeRepository.save(like);

    }
    public List<LikeINFO> getLikeByPost(long postid){
        List<Like> likes=likeRepository.findByPost(postrepo.findById(postid));

        return likes.stream()
                .map(this::mapToLikeinfo)
                .collect(Collectors.toList());
    }
    private LikeINFO mapToLikeinfo(Like like) {
        return LikeINFO.builder()
                .id(like.getId())
                .username(like.getUsername().getUsername())
                .postid(like.getPost().getId())
                .build();
    }
}
