package askapp.auth;

import askapp.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class userinfo {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String sexe;

    private Long id;

    private Role role;
    private String imageid;
    private String filetype;
}

