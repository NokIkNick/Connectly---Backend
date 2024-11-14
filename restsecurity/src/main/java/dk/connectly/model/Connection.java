package dk.connectly.model;

import java.util.Set;

import dk.connectly.utils.ConnectionType;
import dk.connectly.utils.ConnectionTypeSetConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="connection")
@ToString
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User firstUser;

    @ManyToOne
    private User secondUser;

    @Convert(converter = ConnectionTypeSetConverter.class)
    private Set<ConnectionType> connectionType;
}
