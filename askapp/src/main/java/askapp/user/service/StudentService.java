package askapp.user.service;

import askapp.user.models.Student;
import askapp.user.usersrepo.StudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    StudRepo studRepo;

    public String getClass(long id){
        Student student=studRepo.findById(id);
        return  student.getClassse();
    }
}
