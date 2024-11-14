package askapp.auth;


import askapp.file.file;
import askapp.file.fileService;
import askapp.user.Role;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService serviceuser ;
    private final UserRepository repo ;
    private final fileService Fileservice;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<registerresponse> register(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,

            @RequestParam(required = false) String password,

            @RequestParam(required = false) Role role,

            @RequestParam(required = false) String classse,
            @RequestParam(required = false) String bio,

            @RequestParam(required = false) MultipartFile image){
    try {

        file pdp = Fileservice.saveAttachment(image);
        RegisterRequest request = RegisterRequest.builder()
                .nom(nom)
                .prenom(prenom)
                .email(email)
                .bio(bio)
                .image(pdp)
                .classse(classse)
                .password(password)
                .role(role)
                .build();
        registerresponse saved = authenticationService.register(request);
        return ResponseEntity.ok(saved);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }}

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {

        return ResponseEntity.ok(serviceuser.authenticate(request));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<userinfo> getUserById(@PathVariable("id") Long id) {
        userinfo user = serviceuser.finduserById2(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/find2/{id}")
    public ResponseEntity<User> getUserById2(@PathVariable("id") Long id) {
        User user = serviceuser.finduserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/changepass")
    public ResponseEntity<changepasswordresponse> changePassword(
            @RequestBody changepasswordrequest request) throws Exception {
        return ResponseEntity.ok(serviceuser.changePassword(request));
    }

    @PostMapping("/forgetpass")
    public ResponseEntity<forgetpassresponse> forgetPassword(
            @RequestBody forgetpassrequest request
    ) throws Exception {

        return ResponseEntity.ok(serviceuser.forgetPassword(request));
    }

    @GetMapping("/users")


    public ResponseEntity<List<User>> getAllusers() {
        List<User> userr = serviceuser.findAllUsers();
        return new ResponseEntity<>(userr, HttpStatus.OK);
    }

    @GetMapping("/users2")
    public ResponseEntity<List<userinfo>> getAllusers2() throws Exception {
        List<userinfo> userr = serviceuser.findAllUsers1();
        return new ResponseEntity<>(userr, HttpStatus.OK);
    }

    @PostMapping("/forgetpass2")
    public ResponseEntity<forgetpass2response> forgetpass2(
            @RequestBody fogetpass2request request) throws Exception {
        return ResponseEntity.ok(serviceuser.forgetpass2(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) throws Exception {
        serviceuser.deleteUserAndTokensById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
                                           @RequestParam(required = false) String nom,
                                           @RequestParam(required = false) String prenom,
                                           @RequestParam(required = false) String email,

                                           @RequestParam(required = false) String password,


                                           @RequestParam(required = false) String classse,
                                           @RequestParam(required = false) String bio,

                                           @RequestParam(required = false) MultipartFile image) {
        try {
            file pdp = null;
            if (image != null) {
                pdp =  Fileservice.saveAttachment(image);
            }
            RegisterRequest request = RegisterRequest.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .email(email)
                    .bio(bio)
                    .image(pdp)


                    .classse(classse)
                    .password(password)
                    .build();
            User saved = authenticationService.updateUser(id,request);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/findbymail/{email}")
    public ResponseEntity<User> getUserBymail(@PathVariable("email") String email) {
        User user = serviceuser.finduserByemail(email);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/findbymail2/{email}")
    public ResponseEntity<userinfo> getUserBymail2(@PathVariable("email") String email) {
        userinfo user = serviceuser.finduserByemail2(email);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }


    @GetMapping("/employee/total")
    public Long getTotalUsers() {

        return repo.count();
    }

}

