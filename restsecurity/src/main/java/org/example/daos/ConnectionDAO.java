package org.example.daos;

import org.example.dtos.ConnectionDTO;
import org.example.dtos.ConnectionRequestDTO;
import org.example.model.Connection;

public class ConnectionDAO extends DAO<Connection, Integer> {

    private static TestDAO instance;

    public static TestDAO getInstance(boolean isTesting) {
        if (instance == null) {
            instance = new TestDAO(isTesting);
        }
        return instance;
    }

    ConnectionDAO(boolean isTesting){
        super(Connection.class, isTesting);
    }

    public ConnectionDTO acceptRequest(ConnectionRequestDTO CRDTO){
        
        try(var em = emf.createEntityManager()){
            Connection conn = em.createQuery("select c from connection c where c.connector = " + CRDTO.getConnector() + " and c.connection = " + CRDTO.getConnection(), Connection.class).getSingleResult();
            if(conn != null){
                ConnectionDTO CDTO = new ConnectionDTO(conn);
                return CDTO;
            }
            conn = new Connection(CRDTO, em);
            ConnectionRequestDTO connReqBack = new ConnectionRequestDTO(CRDTO.getConnection(), CRDTO.getConnector(), CRDTO.getTypes());
            Connection connBack = new Connection(connReqBack, em);
            create(conn);
            create(connBack);
            return new ConnectionDTO(connBack);
        }
    }

}
