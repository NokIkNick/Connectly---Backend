package dk.connectly.daos;

//import dk.connectly.dtos.ConnectionDTO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Connection;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.model.User;
import jakarta.persistence.EntityExistsException;

public class ConnectionDAO extends DAO<Connection, Integer> {

    private static ConnectionDAO instance;

    public static ConnectionDAO getInstance(boolean isTesting) {
        if (instance == null) {
            instance = new ConnectionDAO(isTesting);
            connectionRequestDAO = ConnectionRequestDAO.getInstance(isTesting);
        }
        return instance;
    }

    private static ConnectionRequestDAO connectionRequestDAO;

    ConnectionDAO(boolean isTesting){
        super(Connection.class, isTesting);
    }

    public boolean acceptRequest(ConnectionRequestDTO CRDTO) throws EntityExistsException, ApiException {
        
        try(var em = emf.createEntityManager()){
            //make sure they both exist.
            User existingFirstUser = em.find(User.class, CRDTO.getRequester().getId());
            User existingSecondUser = em.find(User.class, CRDTO.getReceiver().getId());

            // if either don't exist
            if(existingFirstUser == null || existingSecondUser == null){
                // find out which case it is
                String userText = (existingFirstUser == null && existingSecondUser == null) ? "users" : existingFirstUser == null ? "logged in user" : "receiver";

                throw new ApiException(400, userText + " don't exist");
            }

            // Checks to make sure the Connection doesn't already exists
            boolean connExists = em.createQuery("select c from Connection c where c.firstUser in (?1, ?2) or c.connection in (?1, ?2)", Connection.class)
                    .setParameter(1, existingFirstUser)
                    .setParameter(2, existingSecondUser)
                    .getResultList().size() != 0;

            if(connExists){
                //return new ConnectionDTO(conn);
                return false;
            }

            // find CR
            ConnectionRequest CR = connectionRequestDAO.getById(CRDTO.getId());

            // construct and persist the connection
            Connection conn = new Connection();
            conn.setFirstUser(CR.getRequester());
            conn.setSecondUser(CR.getReceiver());
            conn.setConnectionType(CRDTO.getTypes());
            create(conn);

            // delete the request
            connectionRequestDAO.delete(CR.getId());
            //ConnectionRequestDTO connReqBack = new ConnectionRequestDTO(CRDTO.getReceiver(), CRDTO.getRequester(), CRDTO.getTypes());
            return true;
        }
    }

}
