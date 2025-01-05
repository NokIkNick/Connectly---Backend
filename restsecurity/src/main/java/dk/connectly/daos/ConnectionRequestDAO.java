package dk.connectly.daos;

import java.util.List;
import java.util.Set;

import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Connection;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;

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

    public ConnectionRequestDTO setupNewRequest(UserDTO firstUser, UserDTO secondUser, Set<ConnectionType> types) throws EntityExistsException, ApiException{
        
        try(var em = emf.createEntityManager()){
            //make sure they both exist.
            User existingFirstUser = em.find(User.class, firstUser.getId());
            User existingSecondUser = em.find(User.class, secondUser.getId());

            // if either don't exist
            if(existingFirstUser == null || existingSecondUser == null){
                // find out which case it is
                String userText = (existingFirstUser == null && existingSecondUser == null) ? "users" : existingFirstUser == null ? "logged in user" : "receiver";

                throw new ApiException(400, userText + " don't exist");
            }

            // Check if it already exists, we don't want duplicates in case the request is sent twice.
            boolean CRexists = getBy2Relations(em, existingFirstUser, existingSecondUser).size() != 0;

            if(CRexists){
                throw new EntityExistsException("Connection Request");
            }

            // Check if they already have a connection
            boolean Cexists = em.createQuery("select c from Connection c where c.firstUser in (?1, ?2) and c.secondUser in (?1, ?2)", Connection.class)
                    .setParameter(1, existingFirstUser)
                    .setParameter(2, existingSecondUser)
                    .getResultList().size() != 0;

            if(Cexists){
                throw new EntityExistsException("Connection");
            }

            // Persist Connection Request
            ConnectionRequest CR = new ConnectionRequest(existingFirstUser, existingSecondUser, types);
            create(CR);
            return new ConnectionRequestDTO(CR);
        }
    }

    public List<ConnectionRequest> getBy2Relations(EntityManager em, User firstUser, User secondUser){
        return em.createQuery("select cr from ConnectionRequest cr where cr.requester in (?1, ?2) and cr.receiver in (?1, ?2)",
                         ConnectionRequest.class)
                    .setParameter(1, firstUser)
                    .setParameter(2, secondUser)
                    .getResultList();
    }

    public List<ConnectionRequest> getByReciever(UserDTO receiver) {
        try(var em = emf.createEntityManager()){
            User us = em.find(User.class, receiver.getId());
            return em.createQuery("select cr from ConnectionRequest cr where cr.receiver = ?1",
                            ConnectionRequest.class)
                        .setParameter(1, us)
                        .getResultList();
        }
    }
}
