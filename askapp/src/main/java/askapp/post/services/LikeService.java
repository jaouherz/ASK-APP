package askapp.post.services;

import askapp.post.Models.Like;
import askapp.post.Models.ModelsINFO.LikeINFO;
import askapp.post.Models.ModelsINFO.PostINFO;
import askapp.post.Models.Post;
import askapp.post.repositories.LikeRepository;
import askapp.post.repositories.Postrepo;
import askapp.user.models.User;
import askapp.user.usersrepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    Postrepo postrepo;
    @Autowired
    UserRepository userRepository;
    public LikeINFO pressLike(long postID,long userID){

        Post post = postrepo.findById(postID).orElseThrow(() -> new RuntimeException("Post not found"));
        User user=userRepository.findById(userID).get();
        Like like= this.likeRepository.findByUsernameAndAndPost(user,post);
        if(like!=null){
            this.likeRepository.delete(like);
            return  mapToLikeinfo(like);
        }
        like=new Like();
        like.setPost(post);
        like.setUsername(user);
        like=likeRepository.save(like);
        post.getLikes().add(like);
        postrepo.save(post);
        return mapToLikeinfo(like);
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
                .userID(like.getUsername().getId())
                .postid(like.getPost().getId())
                .build();
    }
}
