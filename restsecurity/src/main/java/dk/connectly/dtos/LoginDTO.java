package dk.connectly.dtos;

import dk.connectly.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
  private String email;
  private String password;

  LoginDTO(User user){
    this.email = user.getEmail();
    this.password = user.getPassword();
  }
}
