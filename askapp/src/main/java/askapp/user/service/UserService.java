package askapp.user.service;

import askapp.user.models.User;
import askapp.user.usersrepo.AdminRepo;
import askapp.user.usersrepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepo adminRepo;

    public String getUserNameById(long id){
        return this.userRepository.findUserById(id).getUsernamez();
    }
}
