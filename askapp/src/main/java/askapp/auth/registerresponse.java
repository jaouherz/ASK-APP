package askapp.auth;

import askapp.user.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class registerresponse {
    private String token;
    private String nom;
    private String prenom;
    private String email;
    private String password;


    private Role role;
    private String msg;
}


