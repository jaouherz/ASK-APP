package askapp.post.blacklist.service;

import askapp.post.blacklist.exception.BlackListDoesntExistException;
import askapp.post.blacklist.exception.BlacklistAlreadyExistsException;
import askapp.post.blacklist.models.Blacklist;
import askapp.post.blacklist.repositories.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlackListService {
    @Autowired
    BlacklistRepository blacklistRepository;

    public Blacklist addWord(Blacklist blacklist) throws Exception {
        Optional<Blacklist> blacklist1=this.blacklistRepository.findBlacklistByWord(blacklist.getWord());
        if(blacklist1.isPresent()){
            throw new BlacklistAlreadyExistsException("Blacklist entry already exists for word: " + blacklist.getWord());
        }
        blacklist.setDate(new Date());
        return this.blacklistRepository.save(blacklist);
    }
    public List<Blacklist> getAll(){
        return this.blacklistRepository.findAll();
    }
    public Blacklist update(long id , String word){
        Optional<Blacklist> blacklist=this.blacklistRepository.findById(id);
        if(blacklist.isPresent()){
            blacklist.get().setWord(word);
            return this.blacklistRepository.save(blacklist.get());
        }
        throw new BlackListDoesntExistException("Blacklist entry doesn t exists for id: " + id);

    }
    public void delete(long id){
        Optional<Blacklist> blacklist=this.blacklistRepository.findById(id);
        this.blacklistRepository.delete(blacklist.get());
    }
    public Blacklist getById(long id){
        return  this.blacklistRepository.findById(id).get();
    }
}
