package dk.connectly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
    public class Role {
        @Id
        @Column(name = "name",nullable = false)
        private String name;

        @ManyToMany(mappedBy ="roles")
        @JsonIgnore
        private Set<User> users = new HashSet<>();

        public Role(String name){
            this.name = name;
        }

        public void addUser(User user) {
            if(user != null && !this.users.contains(user)){
                this.users.add(user);
                user.addRole(this);
            }
        }

}
