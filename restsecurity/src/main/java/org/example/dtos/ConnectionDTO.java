package org.example.dtos;

import lombok.*;

import java.util.Set;

import org.example.model.Connection;
import org.example.model.User;
import org.example.utils.ConnectionType;

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
        this.connector = conn.getConnector();
        this.connection = conn.getConnection();
        this.types = conn.getTypes();
    }

}
