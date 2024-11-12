package dk.connectly.model;

import dk.connectly.utils.ConnectionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User firstUser;

    @ManyToOne
    private User secondUser;

    @Enumerated(EnumType.STRING)
    private ConnectionType connectionType;

}
