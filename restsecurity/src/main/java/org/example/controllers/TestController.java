package org.example.controllers;

import io.javalin.http.Handler;
import org.example.daos.IDAO;
import org.example.daos.MemoryDAO;
import org.example.daos.TestMemoryDao;
import org.example.dtos.TestDTO;

public class TestController {

    private static TestController instance;

    private static TestMemoryDao testDao = TestMemoryDao.getInstance();

    public static TestController getInstance() {
        if (instance == null) {
            instance = new TestController();
        }
        return instance;
    }


    public Handler getAllTest(){
        return ctx -> {
            ctx.json(testDao.getAll());
        };
    }

    public Handler createTest(){
        return ctx -> {
            TestDTO body = ctx.bodyAsClass(TestDTO.class);
            TestDTO created = testDao.create(body);
            ctx.json(created);
        };
    }

}
