package dk.connectly.controllers;
import dk.connectly.daos.DAO;
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
            if (blockingUser.getEmail().equals(blockedUser.getEmail())) {
                ctx.status(400);
                throw new ApiException(400, "You can't block yourself");
            } else if (blockingUser.isBlocked(blockedUser)) {
                ctx.status(400);
                throw new ApiException(400, "User is already blocked");
            }
            blockingUser.blockUser(blockedUser);
            securityDao.update(blockingUser, blockingUser.getEmail());
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

            if (!blockingUser.isBlocked(blockedUser)) {
                ctx.status(400);
                throw new ApiException(400, "User is not blocked");
            }
            blockingUser.unblockUser(blockedUser);
            securityDao.update(blockingUser, blockingUser.getEmail());

            ctx.status(201);
            ctx.json(blockingDTO);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while unblocking user" + e.getMessage());
            }
        };
    }
}
