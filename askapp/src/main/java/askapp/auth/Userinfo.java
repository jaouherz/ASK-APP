package askapp.auth;

import askapp.file.File;
import askapp.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Userinfo {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String bio ;
    private Long id;
    private String usernamez ;
    private Role role;
    private boolean isactive ;
    private String pdp ;
    private File image;

}

