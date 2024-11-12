package dk.connectly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name ="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "firstUser")
    private List<Connection> connections = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();


    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "user_email",referencedColumnName = "email")},inverseJoinColumns = {
            @JoinColumn(name = "role_name",referencedColumnName = "name")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User(String password, String email){
        this.email = email;
        this.password= password;
        String salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password,salt);

    }
    public User(String password, Role roles, String email){
        this.email = email;
        String salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password,salt);
        addRole(roles);

    }
    public void addRole(Role role){
        if(role != null && !roles.contains(role)){
            roles.add(role);
            role.addUser(this);
        }
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public Set<String> getRolesAsString(){
        if(roles.isEmpty()){
            return null;
        }
        Set<String> rolesAsString = new HashSet<>();
        roles.forEach((role) -> {
            rolesAsString.add(role.getName());
        });
        return rolesAsString;
    }
    public boolean verifyUser(String password){
        return BCrypt.checkpw(password,this.password);
    }

    public String rolesToString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Role role : roles) {
            stringBuilder.append(role.getName()).append(", ");
        }

        // Fjerner det sidste komma og mellemrum
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }

        return stringBuilder.toString();
    }
}