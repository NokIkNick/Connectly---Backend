package dk.connectly.daos;

import java.util.Set;

import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.TestDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.utils.ConnectionType;

public class TestDAO extends DAO<TestDTO, Integer> {

    private static TestDAO instance;

    public static TestDAO getInstance(boolean isTesting) {
        if (instance == null) {
            instance = new TestDAO(isTesting);
        }
        return instance;
    }

    TestDAO(boolean isTesting){
        super(TestDTO.class, isTesting);
    }

}
