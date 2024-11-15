package dk.connectly.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import dk.connectly.model.User;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String email;

    private String firstName;
    private String lastName;
    private Set<String> roles;

    public UserDTO(String email, Set<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public UserDTO(User user){
        this.email = user.getEmail();
        this.roles = user.getRolesAsString();
    }


    public String getId(){
        return getEmail();
    }

    @Override
    public String toString(){
        return this.email;
    }


}
