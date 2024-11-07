package org.example.daos;

import jakarta.persistence.EntityNotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.Role;
import org.example.model.User;

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
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        try(var em = emf.createEntityManager()){
            Role role = em.find(Role.class, "USER");
            if(role == null){
                role = new Role("USER");
                em.persist(role);
            }
            user.addRole(role);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
        return user;
    }


    public User getVerifiedUser(String username, String password) throws ValidationException {
        try(var em = emf.createEntityManager()){
            List<User> users = em.createQuery("select u from users u", User.class).getResultList();
            users.stream().forEach(user -> System.out.println(user.getUsername()+" "+user.getPassword()));
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
