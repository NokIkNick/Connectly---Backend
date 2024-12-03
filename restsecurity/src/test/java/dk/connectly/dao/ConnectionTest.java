package dk.connectly.dao;

import org.junit.jupiter.api.Test;

import dk.connectly.daos.ConnectionDAO;
import dk.connectly.daos.ConnectionRequestDAO;
import dk.connectly.daos.DAO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.model.Connection;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
public class ConnectionTest {
  @BeforeAll
  public static void setup(){
    cdao = ConnectionDAO.getInstance(true);
    crdao = ConnectionRequestDAO.getInstance(true);

    /// setup users & a anonumous dao as it's not created yet elsewhere.
    DAO<User, Integer> userdao = new DAO<User,Integer>(User.class, true){
      
    };
    user1 = new User("test1", "testConnectionTest1@test.dk");
    user2 = new User("test2", "testConnectionTest2@test.dk");

    userdao.create(user1);
    userdao.create(user2);
  }

  @BeforeEach
  public void setupEach(){

  }

  @AfterEach
  public void cleanupEach(){
    for (Connection conn : cdao.getAll()) {
      cdao.delete(conn.getId());
    }
  }

  private static ConnectionDAO cdao;
  private static ConnectionRequestDAO crdao;

  private static User user1;
  private static User user2;

  @Test
  public void testAccept(){
      /// arrange
    /// get initial count as we need to assert on it later.
    int initialCount = cdao.getAll().size();
    int initialCountCR = crdao.getAll().size();

    /// Create a request
    ConnectionRequest cr = new ConnectionRequest(user1, user2, Set.of(ConnectionType.FAMILY));
    crdao.create(cr);
    assertEquals(initialCountCR + 1, crdao.getAll().size(), "Connection Request not made.");

      /// act
    //accept
    assertDoesNotThrow(() -> cdao.acceptRequest(new ConnectionRequestDTO(cr)), "AcceptRequest threw an error.");

      /// assert
    assertEquals(initialCount +1, cdao.getAll().size(), "Connection not made.");
    assertEquals(initialCountCR, crdao.getAll().size(), "Connection Request not deleted.");
  }

}
