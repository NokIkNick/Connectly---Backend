package dk.connectly.daos;

import java.util.Set;

import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;

import jakarta.persistence.EntityExistsException;

public class ConnectionRequestDAO extends DAO<ConnectionRequest, Integer> {

    private static ConnectionRequestDAO instance;

    public static ConnectionRequestDAO getInstance(boolean isTesting) {
        if (instance == null) {
            instance = new ConnectionRequestDAO(isTesting);
        }
        return instance;
    }

    private ConnectionRequestDAO(boolean isTesting){
        super(ConnectionRequest.class, isTesting);
    }

    public void setupNewRequest(UserDTO firstUser, UserDTO secondUser, Set<ConnectionType> types) throws EntityExistsException, ApiException{
        
        try(var em = emf.createEntityManager()){
            ConnectionRequest CR = em.createQuery("select cr from ConnectionRequest cr where c.connector = " + firstUser.getEmail() + " and c.connection = " + secondUser.getEmail(), ConnectionRequest.class).getSingleResult();
            if(CR != null){
                throw new EntityExistsException();
            }
            ConnectionRequestDTO CRDTO = new ConnectionRequestDTO(firstUser, secondUser, types);
            User _firstUser = em.find(User.class, firstUser.getId());
            User _secondUser = em.find(User.class, secondUser.getId());
            CR = new ConnectionRequest(_firstUser, _secondUser, types);
            create(CR);
            return;
        }
    }

}
