package org.example.daos;

import org.example.dtos.TestDTO;

public class TestMemoryDao extends MemoryDAO<TestDTO, Integer> {

    private static TestMemoryDao instance;

    public static TestMemoryDao getInstance() {
        if (instance == null) {
            instance = new TestMemoryDao();
        }
        return instance;
    }


}
