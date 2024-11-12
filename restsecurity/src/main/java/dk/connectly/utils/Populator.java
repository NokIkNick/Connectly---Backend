package dk.connectly.utils;

import dk.connectly.model.*;
import dk.connectly.utils.ConnectionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Populator {

    private final EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void populate() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Create Roles
            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");
            em.persist(adminRole);
            em.persist(userRole);

            // Create Topics
            Topic techTopic = new Topic();
            techTopic.setName("Technology");
            Topic scienceTopic = new Topic();
            scienceTopic.setName("Science");
            em.persist(techTopic);
            em.persist(scienceTopic);

            // Create Users
            User user1 = new User("password1", "user1@example.com");
            user1.setFirstName("Alice");
            user1.setLastName("Anderson");
            user1.addRole(userRole);

            User user2 = new User("password2", "user2@example.com");
            user2.setFirstName("Bob");
            user2.setLastName("Brown");
            user2.addRole(userRole);

            User admin = new User("adminpass", "admin@example.com");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.addRole(adminRole);

            em.persist(user1);
            em.persist(user2);
            em.persist(admin);

            // Create Posts
            Post post1 = new Post();
            post1.setAuthor(user1);
            post1.setDate_created(new Date());
            post1.setTitle("Latest in Technology");
            post1.setContent("Content about technology...");
            post1.setVisibility(ConnectionType.PUBLIC);
            post1.setTopics(List.of(techTopic));
            em.persist(post1);

            Post post2 = new Post();
            post2.setAuthor(user2);
            post2.setDate_created(new Date());
            post2.setTitle("The Science Behind Climate Change");
            post2.setContent("Content about science...");
            post2.setVisibility(ConnectionType.FAMILY);
            post2.setTopics(List.of(scienceTopic));
            em.persist(post2);

            // Create Connections
            Connection connection1 = new Connection();
            connection1.setFirstUser(user1);
            connection1.setSecondUser(user2);
            connection1.setConnectionType(ConnectionType.FRIEND);
            em.persist(connection1);

            Connection connection2 = new Connection();
            connection2.setFirstUser(admin);
            connection2.setSecondUser(user1);
            connection2.setConnectionType(ConnectionType.WORK);
            em.persist(connection2);

            Connection connection3 = new Connection();
            connection3.setFirstUser(admin);
            connection3.setSecondUser(user1);
            connection3.setConnectionType(ConnectionType.FAMILY);
            em.persist(connection3);

            tx.commit();
            System.out.println("Database populated successfully with sample data.");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}