package dk.connectly.daos;

//import dk.connectly.dtos.ConnectionDTO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.model.Connection;

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
            String connectors = CRDTO.getRequester() + ", " + CRDTO.getReceiver();
            Connection conn = em.createQuery("select c from connection c where c.connector in (" + connectors + ") or c.connection in (" + connectors + ")", Connection.class).getSingleResult();
            if(conn != null){
                //return new ConnectionDTO(conn);
                return false;
            }
            conn = em.find(Connection.class, CRDTO.getId());
            ConnectionRequestDTO connReqBack = new ConnectionRequestDTO(CRDTO.getReceiver(), CRDTO.getRequester(), CRDTO.getTypes());
            create(conn);
            return true;
        }
    }

}
