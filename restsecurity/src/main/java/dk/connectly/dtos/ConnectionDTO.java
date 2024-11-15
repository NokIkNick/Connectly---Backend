package dk.connectly.dtos;

import lombok.*;

import java.util.Set;

import dk.connectly.model.Connection;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;
import dk.connectly.utils.ConnectionTypeSetConverter;
import jakarta.persistence.Convert;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionDTO implements DTO<Integer> {
    Integer id;
    User firstUser;
    User secondUSer;

    @Convert(converter = ConnectionTypeSetConverter.class)
    Set<ConnectionType> types;

    public ConnectionDTO(Connection conn){
        this.id = conn.getId();
        this.firstUser = conn.getFirstUser();
        this.secondUSer = conn.getSecondUser();
        this.types = conn.getConnectionType();
    }

}
