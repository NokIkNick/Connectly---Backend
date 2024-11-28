package dk.connectly.controllers;

//import dk.connectly.daos.TestMemoryDao;
import dk.connectly.dtos.TestDTO;
import io.javalin.http.Handler;
import dk.connectly.daos.TestDAO;

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
