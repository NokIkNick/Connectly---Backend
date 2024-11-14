package dk.connectly.dtos;

import lombok.*;

import java.util.Set;

import dk.connectly.model.Connection;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionDTO implements DTO<Integer> {
    Integer id;
    User connector;
    User connection;
    Set<ConnectionType> types;

    public ConnectionDTO(Connection conn){
        this.id = conn.getId();
        this.connector = conn.getFirstUser();
        this.connection = conn.getSecondUser();
        this.types = conn.getConnectionType();
    }

}
