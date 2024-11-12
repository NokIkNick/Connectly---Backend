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

    private String username;
    private String password;
    private Set<String> roles;

    public UserDTO(String username, String password,Set<String> roles){
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserDTO(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public UserDTO(User user){
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRolesAsString();
    }



}