package org.example.controllers;

import io.javalin.http.Handler;
import org.example.daos.TestDAO;
import org.example.dtos.TestDTO;

public class TestController {

    private static TestController instance;

    private static TestDAO testDao;

    public static TestController getInstance(boolean isTesting) {
        if (instance == null) {
            testDao = TestDAO.getInstance(isTesting );
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
