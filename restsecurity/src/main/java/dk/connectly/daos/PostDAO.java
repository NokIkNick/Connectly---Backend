package dk.connectly.daos;

import dk.connectly.dtos.PostDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Connection;
import dk.connectly.model.Post;
import dk.connectly.model.User;

import java.util.List;

public class PostDAO extends DAO<Post,Integer> {

    private static PostDAO instance;

        public PostDAO() {
            super(Post.class, false);
        }

        public static PostDAO getInstance() {
            if(instance == null){
                instance = new PostDAO();
            }
            return instance;
        }

    public List<PostDTO> getPostsByVisibility(String visibility, User user) throws ApiException {
            try (var em = emf.createEntityManager()) {
                List<Connection> connections = em.createQuery("select c from Connection c where c.firstUser = :user and c.connectionType = :visibility", Connection.class)
                        .setParameter("user", user)
                        .setParameter("visibility", visibility)
                        .getResultList();

                List<User> secondUsers = connections.stream()
                        .map(Connection::getSecondUser)
                        .toList();

                List<Post> posts = em.createQuery("select p from Post p where p.author in :users and p.visibility = :visibility", Post.class)
                        .setParameter("users", secondUsers)
                        .setParameter("visibility", visibility)
                        .getResultList();

                return posts.stream()
                        .map(PostDTO::new)
                        .toList();
            } catch (Exception e) {
                throw new ApiException(500, "Error while getting posts" + e.getMessage());
            }
    }
}

