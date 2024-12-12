package dk.connectly.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlockingDTO {
    private String blocking_email;
    private String blocked_email;

    public BlockingDTO(String blocking_email, String blocked_email) {
        this.blocking_email = blocking_email;
        this.blocked_email = blocked_email;
    }


}
