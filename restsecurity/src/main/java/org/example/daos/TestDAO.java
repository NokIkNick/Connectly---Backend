package org.example.daos;

import java.util.Set;

import org.example.dtos.ConnectionRequestDTO;
import org.example.dtos.TestDTO;
import org.example.dtos.UserDTO;
import org.example.model.ConnectionRequest;
import org.example.utils.ConnectionType;

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
