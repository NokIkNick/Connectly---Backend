package dk.connectly.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import dk.connectly.utils.ConnectionType;
import dk.connectly.utils.ConnectionTypeSetConverter;

@Table(name ="connectionRequest")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ConnectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private User requester;

    @Convert(converter = ConnectionTypeSetConverter.class)
    private Set<ConnectionType> types;

    private boolean isSeen;
    public ConnectionRequest(User connector, User connection, Set<ConnectionType> types) {
        this.types = types;
        this.requester = connector;
        this.receiver = connection;
    }

}
