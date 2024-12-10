package dk.connectly.dao;

import org.junit.jupiter.api.Test;

import dk.connectly.daos.ConnectionDAO;
import dk.connectly.daos.ConnectionRequestDAO;
import dk.connectly.daos.DAO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Connection;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;
import jakarta.persistence.EntityExistsException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Set;

public class ConnectionRequestTest {
  @BeforeAll
  public static void setup(){
    crdao = ConnectionRequestDAO.getInstance(true);
    /// setup users & a anonumous dao as it's not created yet elsewhere.
    DAO<User, String> userdao = new DAO<User, String>(User.class, true){
      
    };
    user1 = new User("test1", "testConnectionRequestTest1@test.dk");
    user2 = new User("test2", "testConnectionRequestTest2@test.dk");

    userdao.create(user1);
    userdao.create(user2);
  }

  @BeforeEach
  public void setupEach(){
    
  }

  @AfterEach
  public void cleanupEach(){
    for (ConnectionRequest conn : crdao.getAll()) {
      crdao.delete(conn.getId());
    }
  }

  private static ConnectionRequestDAO crdao;

  private static User user1;
  private static User user2;

  @Test
  public void testCreateConnection(){
      /// arrange
    /// get initial count as we need to assert on it later.
    int initialCount = crdao.getAll().size();

      /// act
    /// Create a request
    ConnectionRequest cr = new ConnectionRequest(user1, user2, Set.of(ConnectionType.FAMILY));
    crdao.create(cr);

      /// assert
    assertEquals(initialCount + 1, crdao.getAll().size(), "Connection Request not made.");
  }
}