package askapp.user;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder

@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    @Builder.Default
    private Role role = Role.STUD;
    private  String classe ;
}
