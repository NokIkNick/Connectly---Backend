package dk.connectly.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import dk.connectly.model.User;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String email;
    private String password;
    private Set<String> roles;

    public UserDTO(String email, String password, Set<String> roles){
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserDTO(String email, Set<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public UserDTO(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRolesAsString();
    }



}
