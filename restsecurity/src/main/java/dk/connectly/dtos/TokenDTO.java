package dk.connectly.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {

    private String token;
    private String username;
    private String firstName;
    private String lastName;


}
