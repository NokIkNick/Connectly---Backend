package dk.connectly.daos;

import jakarta.persistence.EntityNotFoundException;
import dk.connectly.exceptions.ValidationException;
import dk.connectly.model.Role;
import dk.connectly.model.User;

import java.util.List;

public class SecurityDao extends DAO<User, String> {

    private static SecurityDao instance;

    private SecurityDao(boolean isTesting) {
        super(User.class, isTesting);
    }

    public static SecurityDao getInstance(boolean isTesting){
        if(instance == null){
            instance = new SecurityDao(isTesting);
        }
        return instance;
    }

    public User createUser(String username, String password){
        User user = new User(password,username);
        try(var em = emf.createEntityManager()){
            Role role = em.find(Role.class, "USER");
            if(role == null){
                role = new Role("USER");
                em.persist(role);
            }
            user.addRole(role);
            create(user);
        }
        return user;
    }


    public User getVerifiedUser(String username, String password) throws ValidationException {
        try(var em = emf.createEntityManager()){
            User user = em.find(User.class, username);
            if(user == null){
                throw new EntityNotFoundException("No user found with username: "+username);
            }
            user.getRoles().size();
            if(!user.verifyUser(password)){
                throw new ValidationException("Error while logging in, invalid credentials");
            }
        return user;
        }
    }

}
