package org.example.daos;

import java.util.Set;

import org.example.dtos.ConnectionRequestDTO;
import org.example.dtos.UserDTO;
import org.example.model.ConnectionRequest;
import org.example.utils.ConnectionType;

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

    public ConnectionRequestDTO setupNewRequest(UserDTO connector, UserDTO connection, Set<ConnectionType> types){
        
        try(var em = emf.createEntityManager()){
            ConnectionRequest CR = em.createQuery("select cr from ConnectionRequest cr where c.connector = " + connector.getEmail() + " and c.connection = " + connection.getEmail(), ConnectionRequest.class).getSingleResult();
            if(CR != null){
                return new ConnectionRequestDTO(CR);
            }
            ConnectionRequestDTO CRDTO = new ConnectionRequestDTO(connector, connection, types);
            return CRDTO;
        }
    }

}
