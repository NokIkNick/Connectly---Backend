package dk.connectly.model;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email")
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "firstUser", fetch = FetchType.EAGER)
    private List<Connection> connections = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private List<ConnectionRequest> connectionRequests = new ArrayList<>();

    @JoinTable(name = "blocked_users", joinColumns = {
            @JoinColumn(name = "blocking_email",referencedColumnName = "email")},inverseJoinColumns = {
            @JoinColumn(name = "blocked_email",referencedColumnName = "email")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> blockedUsers = new ArrayList<>();


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

    public User(String password, String email, String firstName, String lastName){
        this.email = email;
        this.password= password;
        String salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password,salt);
        this.firstName =firstName;
        this.lastName = lastName;

    }

    public void blockUser(User user){
        if(user != null && !isBlocked(user)){
            blockedUsers.add(user);
        }
    }

    public void unblockUser(User user){
        for (User blockedUser : blockedUsers) {
            if (blockedUser.getEmail().equals(user.getEmail())) {
                blockedUsers.remove(blockedUser);
                return;
            }
        }
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

    public boolean isBlocked(User blockedUser) {
        for (User user : blockedUsers) {
            if (user.getEmail().equals(blockedUser.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
