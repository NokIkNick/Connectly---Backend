package dk.connectly.daos;

import dk.connectly.dtos.ConnectionDTO;
//import dk.connectly.dtos.ConnectionDTO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Connection;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.model.User;
import dk.connectly.model.Connection;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

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

    public ConnectionDTO acceptRequest(ConnectionRequestDTO CRDTO) throws EntityExistsException, ApiException {
        
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
            boolean connExists = em.createQuery("select c from " + Connection.class.getName() + " c where c.firstUser in (?1, ?2) and c.secondUser in (?1, ?2)", Connection.class) //  
                    .setParameter(1, existingFirstUser)
                    .setParameter(2, existingSecondUser)
                    .getResultList().size() != 0;


            // find CR
            ConnectionRequest CR;
            try{
                CR = connectionRequestDAO.getById(CRDTO.getId());
            }catch (Exception e) {
                throw new EntityNotFoundException("Connection Request doesn't exist.");
            }

            // if connection already throw error.
            if(connExists){
                // and if the Request exists for some reason, delete it.
                if(CR!=null) connectionRequestDAO.delete(CR.getId());

                throw new EntityExistsException("Connection already exists");
            }

            // construct and persist the connection
            Connection conn = new Connection();
            conn.setFirstUser(CR.getRequester());
            conn.setSecondUser(CR.getReceiver());
            conn.setConnectionType(CRDTO.getTypes());
            create(conn);

            // delete the request
            connectionRequestDAO.delete(CR.getId());

            // return the Connection DTO
            return new ConnectionDTO(conn);
        }
    }

}
