package dk.connectly.model;

import dk.connectly.utils.ConnectionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User author;

    private Date date_created;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private ConnectionType visibility;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Topic> topics;

}
