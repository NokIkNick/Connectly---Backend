package org.example.daos;

import java.util.Set;

import org.example.dtos.ConnectionRequestDTO;
import org.example.dtos.UserDTO;
import org.example.exceptions.ApiException;
import org.example.model.ConnectionRequest;
import org.example.model.User;
import org.example.utils.ConnectionType;

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

    public void setupNewRequest(UserDTO connector, UserDTO connection, Set<ConnectionType> types) throws EntityExistsException, ApiException{
        
        try(var em = emf.createEntityManager()){
            ConnectionRequest CR = em.createQuery("select cr from ConnectionRequest cr where c.connector = " + connector.getEmail() + " and c.connection = " + connection.getEmail(), ConnectionRequest.class).getSingleResult();
            if(CR != null){
                throw new EntityExistsException();
            }
            ConnectionRequestDTO CRDTO = new ConnectionRequestDTO(connector, connection, types);
            User _connector = em.find(User.class, connector.getId());
            User _connection = em.find(User.class, connection.getId());
            CR = new ConnectionRequest(_connector, _connection, types);
            create(CR);
            return;
        }
    }

}
