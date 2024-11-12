package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.example.dtos.ConnectionRequestDTO;
import org.example.utils.ConnectionType;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name ="connection")
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    @Id
    @Column(unique = true, nullable = false)
    private int Id;
    Set<ConnectionType> types;
    User connector;
    User connection;
    
    public Connection(ConnectionRequestDTO CRDTO, EntityManager em) {
        this.connector = em.find(User.class, CRDTO.getConnector().getEmail());
        this.connection = em.find(User.class, CRDTO.getConnection().getEmail());
        this.types = CRDTO.getTypes();
    }
}
