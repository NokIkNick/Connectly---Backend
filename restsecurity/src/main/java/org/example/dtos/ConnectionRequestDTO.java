package org.example.dtos;

import lombok.*;

import java.util.Set;

import org.example.model.ConnectionRequest;
import org.example.utils.ConnectionType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionRequestDTO implements DTO<Integer> {
    Integer id;
    UserDTO connector;
    UserDTO connection;
    Set<ConnectionType> types;

    public ConnectionRequestDTO(UserDTO connector, UserDTO connection, Set<ConnectionType> types){
        this.connector = connector;
        this.connection = connection;
        this.types = types;
    }

    public ConnectionRequestDTO(ConnectionRequest CR){
        id = CR.getId();
        connector = new UserDTO(CR.getConnector());
        connection = new UserDTO(CR.getConnection());
        types = CR.getTypes();
    }
}
