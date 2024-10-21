package askapp.auth;

import askapp.file.file;
import askapp.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Role role;
    private String bio;
    private String usernamez ;
    private String classse ;
    private file image;


}
