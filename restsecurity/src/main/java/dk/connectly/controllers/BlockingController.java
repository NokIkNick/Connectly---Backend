package dk.connectly.controllers;
import dk.connectly.daos.SecurityDao;
import dk.connectly.dtos.BlockingDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.User;
import io.javalin.http.Handler;

public class BlockingController {
    private static BlockingController instance;
    private static SecurityDao securityDao;
    private boolean isTesting;

    private BlockingController() {

    }

    public static BlockingController getInstance(boolean isTesting) {
        if (instance == null) {
            instance = new BlockingController();
            securityDao = SecurityDao.getInstance(isTesting);
        }
        return instance;
    }

    public Handler blockUser() {
        return (ctx) -> {
            try {
            BlockingDTO blockingDTO = ctx.bodyAsClass(BlockingDTO.class);
            User blockingUser = securityDao.getById(blockingDTO.getBlocking_email());
            User blockedUser = securityDao.getById(blockingDTO.getBlocked_email());
            blockingUser.blockUser(blockedUser);
            ctx.status(201);
            ctx.json(blockingDTO);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while blocking user" + e.getMessage());
            }
        };
    }

    public Handler unblockUser() {
        return (ctx) -> {
            try {
            BlockingDTO blockingDTO = ctx.bodyAsClass(BlockingDTO.class);
            User blockingUser = securityDao.getById(blockingDTO.getBlocking_email());
            User blockedUser = securityDao.getById(blockingDTO.getBlocked_email());
            blockingUser.unblockUser(blockedUser);
            ctx.status(201);
            ctx.json(blockingDTO);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while unblocking user" + e.getMessage());
            }
        };
    }
}
