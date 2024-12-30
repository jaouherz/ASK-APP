package askapp.user.controller;

import askapp.user.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")

public class StudentController {
    @Autowired
    StudentService studentService;
    @GetMapping("getclass/{id}")
    public ResponseEntity<String> getClassByStudentId(@PathVariable long id){
        return new ResponseEntity<String>(this.studentService.getClass(id), HttpStatus.OK);
    }
}
