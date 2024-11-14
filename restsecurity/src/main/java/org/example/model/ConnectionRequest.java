package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.example.utils.ConnectionType;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name ="connectionRequest")
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequest {
    @Id
    @Column(unique = true, nullable = false)
    private int Id;
    Set<ConnectionType> types;
    User connector;
    User connection;
    public ConnectionRequest(User connector, User connection, Set<ConnectionType> types) {
        this.types = types;
        this.connector = connector;
        this.connection = connection;
    }
}
