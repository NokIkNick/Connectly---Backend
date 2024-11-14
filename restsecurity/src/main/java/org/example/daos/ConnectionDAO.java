package org.example.daos;

import org.example.dtos.ConnectionDTO;
import org.example.dtos.ConnectionRequestDTO;
import org.example.model.Connection;

public class ConnectionDAO extends DAO<Connection, Integer> {

    private static ConnectionDAO instance;

    public static ConnectionDAO getInstance(boolean isTesting) {
        if (instance == null) {
            instance = new ConnectionDAO(isTesting);
        }
        return instance;
    }

    ConnectionDAO(boolean isTesting){
        super(Connection.class, isTesting);
    }

    public boolean acceptRequest(ConnectionRequestDTO CRDTO){
        
        try(var em = emf.createEntityManager()){
            String connectors = CRDTO.getConnector() + ", " + CRDTO.getConnection();
            Connection conn = em.createQuery("select c from connection c where c.connector in (" + connectors + ") or c.connection in (" + connectors + ")", Connection.class).getSingleResult();
            if(conn != null){
                ConnectionDTO CDTO = new ConnectionDTO(conn);
                return false;
            }
            conn = new Connection(CRDTO, em);
            ConnectionRequestDTO connReqBack = new ConnectionRequestDTO(CRDTO.getConnection(), CRDTO.getConnector(), CRDTO.getTypes());
            create(conn);
            return true;
        }
    }

}
