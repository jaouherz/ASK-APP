package askapp.auth;

import askapp.config.JwtService;
import askapp.email.EmailSender;
import askapp.exeption.UserNotFoundException;
import askapp.file.FileService;
import askapp.token.Token;
import askapp.token.TokenRepository;
import askapp.token.TokenType;
import askapp.user.models.*;
import askapp.user.usersrepo.AdminRepo;
import askapp.user.usersrepo.ProfRepo;
import askapp.user.usersrepo.StudRepo;
import askapp.user.usersrepo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final AdminRepo Adminrep  ;
    private final ProfRepo Profrep;
    private final StudRepo Studrep ;

    private final FileService Fileservice;

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private String idimage;

    public registerresponse register(RegisterRequest request) throws Exception {
        User user = null;

        var existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return registerresponse.builder().msg("The user already exists.").build();
        }

        String username = request.getUsernamez();
        if (username == null || username.isEmpty()) {
            username = generateUniqueUsername(request.getRole());
        }

        var existingUsername = repository.findByUsernamez(username);
        if (existingUsername.isPresent()) {
            return registerresponse.builder().msg("The username already exists.").build();
        }

        if (request.getRole() == Role.ADMIN) {
            user = Admin.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .bio(request.getBio())
                    .usernamez(username)
                    .isactive(true)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            user = Adminrep.save((Admin) user);
        } else if (request.getRole() == Role.PROF) {
            user = Profesor.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .bio(request.getBio())
                    .usernamez(username)

                    .isactive(true)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.PROF)
                    .build();
            user = Profrep.save((Profesor) user);
        } else if (request.getRole() == Role.STUD) {
            user = Student.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .bio(request.getBio())

                    .usernamez(username)
                    .isactive(true)
                    .classse(request.getClassse())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.STUD)
                    .build();
            user = Studrep.save((Student) user);
        }
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        EmailSender emailSender = new EmailSender();
        String subject = "Thank You for Subscribing";
        String body = "Hello " + user.getNom() + ",<br><br>" +
                "Thank you for subscribing! Your account has been successfully created.<br><br>" +
                "Please log in to access your personal space:<br><br>" +
                "<a href=\"http://localhost:4200/login\"><button style=\"background-color:#008CBA;color:white;padding:12px 20px;border:none;border-radius:4px;\">Log In</button></a><br><br>" +
                "Best regards,<br>The Support Team.";
        emailSender.sendEmail(user.getEmail(), subject, body);
        return registerresponse.builder()
                .token(jwtToken)
                .nom(user.getNom())
                .email(user.getEmail())
                .prenom(user.getPrenom())

                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }


    private String generateUniqueUsername(Role role) {
        String username;
        do {
            int randomNum = 1000 + new Random().nextInt(9000);
            username = role.name().toLowerCase() + randomNum;
        } while (repository.findByUsernamez(username).isPresent());
        return username;
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
                .id(user.getId().toString())
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


        String resetPasswordLink = "http://localhost:4200/forgetpass?token=" + jwtToken;


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

    public List<Userinfo> findAllUsers1() throws Exception {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found.");
        }
        List<Userinfo> Userinfos = new ArrayList<>();
        for (User user : users) {
            Userinfo userinfo = new Userinfo();
            userinfo.setId(user.getId());
            userinfo.setNom(user.getNom());
            userinfo.setPrenom(user.getPrenom());
            userinfo.setEmail(user.getEmail());

            userinfo.setPassword(user.getPassword());
            userinfo.setRole(user.getRole());


            Userinfos.add(userinfo);
        }

        return Userinfos;
    }

    @Transactional
    public void deleteUserAndTokensById(Long id) throws Exception {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Token> tokens = tokenRepository.findByUser(user);
            tokenRepository.deleteAll(tokens);

            repository.save(user);
            repository.delete(user);
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }

    public User updateUser(Long id, RegisterRequest newUser) throws Exception {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        if (newUser.getNom() != null) {
            user.setNom(newUser.getNom());
        }
        if (newUser.getPrenom() != null) {
            user.setPrenom(newUser.getPrenom());
        }
        if (newUser.getUsernamez() != null) {
            user.setUsernamez(newUser.getUsernamez());
        }
        if (newUser.getBio() != null) {
            user.setBio(newUser.getBio());
        }
        if (newUser.getImage() != null) {
            user.setImage(newUser.getImage());

        }
        if (newUser.getEmail() != null) {
            var existingEmail = repository.findByEmail(newUser.getEmail());
            if (existingEmail.isPresent() && !existingEmail.get().getId().equals(id)) {
                throw new Exception("The email is already in use by another user.");
            }
            user.setEmail(newUser.getEmail());
        }

        // we have to add student filds here
        if (user instanceof Student) {
            if (newUser.getClassse() != null) {
                ((Student) user).setClassse(newUser.getClassse());
            }
        }
        //hedhi for profffesor
        if (user instanceof Profesor) {
           // if () {

           //}
        }
        //hedhi for admin

        if (user instanceof Admin ) {
            // if () {

            //}
        }

        if (newUser.getUsernamez() != null) {
            var existingUsername = repository.findByUsernamez(newUser.getUsernamez());
            if (existingUsername.isPresent() && !existingUsername.get().getId().equals(id)) {
                throw new Exception("The username is already in use by another user.");
            }
            user.setUsernamez(newUser.getUsernamez());
        }


        return repository.save(user);
    }


    public Userinfo finduserById2(Long id) {
        User user3 = null;

        user3 = repository.findById(id).orElseThrow(() -> new UserNotFoundException("user by id" + id + "notfound"));
        Userinfo userinfo = new Userinfo();
        userinfo.setId(user3.getId());
        userinfo.setNom(user3.getNom());
        userinfo.setPrenom(user3.getPrenom());
        userinfo.setEmail(user3.getEmail());
        userinfo.setUsernamez(user3.getUsernamez());
        userinfo.setBio(user3.getBio());
        userinfo.setPassword(user3.getPassword());
        userinfo.setRole(user3.getRole());
        userinfo.setIsactive(user3.isIsactive());
        userinfo.setPdp(user3.getImage().getId());


        return userinfo;
    }

    public User finduserByemail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user by id" + email + "notfound"));


    }

    public Userinfo finduserByemail2(String email) {
        User user1 = repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user by id" + email + "notfound"));
        Userinfo user = new Userinfo();
        user.setNom(user1.getNom());
        user.setPrenom(user1.getPrenom());
        user.setEmail(user1.getEmail());
        user.setPassword(user1.getPassword());
        user.setRole(user1.getRole());

        return user;
    }
    public Map<String, Long> getUserLoginCount() {
        // Get all tokens from the Token table
        List<Token> allTokens = tokenRepository.findAll();

        // Create a map to hold the count of logins per user
        Map<String, Long> loginCountMap = new HashMap<>();

        // Iterate through the tokens and count logins per user
        for (Token token : allTokens) {
              // Only consider valid tokens
                String username = token.getUser().getUsernamez();
                // Count the number of logins per user (not just counting tokens)
                loginCountMap.put(username, loginCountMap.getOrDefault(username, 0L) + 1);

        }

        // Return the login count for each user
        return loginCountMap;
    }

}
