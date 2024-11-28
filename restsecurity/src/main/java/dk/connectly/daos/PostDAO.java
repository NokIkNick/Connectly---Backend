package dk.connectly.daos;

import dk.connectly.dtos.PostDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Connection;
import dk.connectly.model.Post;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;

import java.util.List;

public class PostDAO extends DAO<Post,Integer> {

    private static PostDAO instance;

        PostDAO(boolean isTesting) {
            super(Post.class, false);
        }

        public static PostDAO getInstance(boolean isTesting) {
            if(instance == null){
                instance = new PostDAO(isTesting);
            }
            return instance;
        }

    public List<PostDTO> getPostsByVisibility(String visibility, User user, int page, int size) throws ApiException {
        if (user == null || visibility == null || visibility.isEmpty()) {
            throw new ApiException(400, "Invalid input: user and visibility must be provided.");
        }
        if (page < 1 || size < 1) {
            throw new ApiException(400, "Invalid pagination: page and size must be greater than 0.");
        }

        try (var em = emf.createEntityManager()) {
            // Convert visibility string to the ConnectionType enum
            ConnectionType connectionType;
            try {
                connectionType = ConnectionType.valueOf(visibility.toUpperCase()); // Convert to enum
            } catch (IllegalArgumentException e) {
                throw new ApiException(400, "Invalid visibility value: " + visibility);
            }

            // Fetch connections based on visibility
            List<Connection> connections = em.createQuery(
                            "select c from Connection c where c.firstUser = :user and c.connectionType = :visibility",
                            Connection.class)
                    .setParameter("user", user)
                    .setParameter("visibility", connectionType)
                    .getResultList();

            if (connections.isEmpty()) {
                return List.of(); // Return empty list if no connections
            }

            // Extract second users
            List<User> secondUsers = connections.stream()
                    .map(Connection::getSecondUser)
                    .toList();

            // Calculate pagination offsets
            int startIndex = (page - 1) * size;

            // Fetch paginated posts for the collected users
            List<Post> posts = em.createQuery(
                            "select p from Post p where p.author in :users and p.visibility = :visibility order by p.date_created desc",
                            Post.class)
                    .setParameter("users", secondUsers)
                    .setParameter("visibility", connectionType)
                    .setFirstResult(startIndex) // Start position
                    .setMaxResults(size) // Number of results to fetch
                    .getResultList();

            // Convert posts to DTOs
            return posts.stream()
                    .map(PostDTO::new) // Ensure PostDTO has a matching constructor
                    .toList();
        } catch (Exception e) {
            throw new ApiException(500, String.format("Error while getting posts for user: %s with visibility: %s. Details: %s",
                    user.getEmail(), visibility, e.getMessage()));
        }
    }








}

