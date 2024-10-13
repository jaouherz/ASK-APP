package askapp.auth;

import askapp.config.JwtService;
import askapp.email.EmailSender;
import askapp.exeption.UserNotFoundException;
import askapp.file.fileService;
import askapp.token.Token;
import askapp.token.TokenRepository;
import askapp.token.TokenType;
import askapp.user.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;


    private final fileService Fileservice;

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private String idimage;

    public registerresponse register(RegisterRequest request) throws Exception {
        User user1 = null;
        var existingUser = repository.findByEmail(request.getEmail());
        var user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .manager(user1)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);
        return registerresponse.builder()
                .token(jwtToken)
                .nom(user.getNom())
                .email(user.getEmail())
                .prenom(user.getPrenom())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public changepasswordresponse changePassword(changepasswordrequest request) throws Exception {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new Exception("Invalid current password");
        }

        if (!isValidPassword(request.getNewPassword())) {
            throw new Exception("Invalid new password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
        EmailSender emailSender = new EmailSender();
        String subject = "Modification du mot de passe";
        String body = "Bonjour " + user.getNom() + ",<br>Nous vous informons que votre mot de passe a été modifié.<br>Si vous n'avez pas modifié votre mot de passe récemment, nous vous recommandons de prendre des mesures immédiates pour protéger votre compte.<br>Cordialement.";
        emailSender.sendEmail(user.getEmail(), subject, body);
        var msga = "salem";
        return changepasswordresponse.builder().msg(msga).email(user.getEmail()).currentPassword(request.getCurrentPassword()).newPassword(request.getNewPassword()).build();
    }

    public forgetpass2response forgetpass2(fogetpass2request request) throws Exception {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found"));


        if (!isValidPassword(request.getNewpassword())) {
            throw new Exception("Invalid new password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewpassword()));
        repository.save(user);
        EmailSender emailSender = new EmailSender();
        String subject = "Modification du mot de passe";
        String body = "Bonjour " + user.getNom() + ",<br>Nous vous informons que votre mot de passe a été modifié.<br>Si vous n'avez pas modifié votre mot de passe récemment, nous vous recommandons de prendre des mesures immédiates pour protéger votre compte.<br>Cordialement.";
        emailSender.sendEmail(user.getEmail(), subject, body);
        var msga = "salem";
        return forgetpass2response.builder().msg(msga).email(user.getEmail()).newPassword(request.getNewpassword()).build();
    }

    private boolean isValidPassword(String newPassword) {
        return true;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .role(user.getRole())
                .token(jwtToken).email(user.getEmail())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public User finduserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("user by id" + id + "notfound"));


    }

    public forgetpassresponse forgetPassword(forgetpassrequest request) throws Exception {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);


        String resetPasswordLink = "http://localhost:4200/mdpoublie?token=" + jwtToken;


        EmailSender emailSender = new EmailSender();
        String subject = "Réinitialiser votre mot de passe";
        String body = "Bonjour " + user.getNom() + ",<br><br>Nous avons reçu une demande de réinitialisation de mot de passe pour votre compte.Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet e-mail.<br><br>Cliquez sur le bouton ci-dessous pour réinitialiser votre mot de passe:<br><br><a href=" + resetPasswordLink + "><button style=\"background-color:#008CBA;color:white;padding:12px 20px;border:none;border-radius:4px;\">Réinitialiser le mot de passe</button></a><br><br>Cordialement";
        emailSender.sendEmail(user.getEmail(), subject, body);
        var msg1 = "gggg";
        return forgetpassresponse.builder().msg(msg1).build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public List<User> findAllUsers() {

        return repository.findAll();
    }

    public List<userinfo> findAllUsers1() throws Exception {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found.");
        }
        List<userinfo> userinfos = new ArrayList<>();
        for (User user : users) {
            userinfo userinfo = new userinfo();
            userinfo.setId(user.getId());
            userinfo.setNom(user.getNom());
            userinfo.setPrenom(user.getPrenom());
            userinfo.setEmail(user.getEmail());

            userinfo.setPassword(user.getPassword());
            userinfo.setRole(user.getRole());
            if (user.getImage() != null) {
                userinfo.setImageid(user.getImage().getId());
                userinfo.setFiletype(user.getImage().getFileType());
            }

            userinfos.add(userinfo);
        }

        return userinfos;
    }

    @Transactional
    public void deleteUserAndTokensById(Long id) throws Exception {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Token> tokens = tokenRepository.findByUser(user);
            tokenRepository.deleteAll(tokens);
            user.setManager(null);
            repository.save(user);
            repository.delete(user);
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }

    public User updateUser(Long id, RegisterRequest newUser) throws Exception {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        if (newUser.getImage() != null) {
            this.idimage = user.getImage().getId();
        }
        if (newUser.getNom() != null) {
            user.setNom(newUser.getNom());
        }
        if (newUser.getPrenom() != null) {
            user.setPrenom(newUser.getPrenom());
        }
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getRole() != null) {
            user.setRole(newUser.getRole());
        }

        if (newUser.getImage() != null) {
            user.setImage(newUser.getImage());
        }
        User user4 = repository.save(user);
        if (newUser.getImage() != null && this.idimage != null) {
            Fileservice.deleteAttachment(this.idimage);

        }
        return user4;
    }

    public userinfo finduserById2(Long id) {
        User user3 = null;

        user3 = repository.findById(id).orElseThrow(() -> new UserNotFoundException("user by id" + id + "notfound"));
        userinfo userinfo = new userinfo();
        userinfo.setId(user3.getId());
        userinfo.setNom(user3.getNom());
        userinfo.setPrenom(user3.getPrenom());
        userinfo.setEmail(user3.getEmail());

        userinfo.setPassword(user3.getPassword());
        userinfo.setRole(user3.getRole());
        if (user3.getImage() != null) {
            userinfo.setImageid(user3.getImage().getId());
            userinfo.setFiletype(user3.getImage().getFileType());
        }

        return userinfo;
    }

    public User finduserByemail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user by id" + email + "notfound"));


    }

    public userinfo finduserByemail2(String email) {
        User user1 = repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user by id" + email + "notfound"));
        userinfo user = new userinfo();
        user.setNom(user1.getNom());
        user.setPrenom(user1.getPrenom());
        user.setEmail(user1.getEmail());
        user.setPassword(user1.getPassword());
        user.setRole(user1.getRole());
        user.setImageid(user1.getImage().getId());
        user.setFiletype(user1.getImage().getFileType());
        return user;
    }

}
