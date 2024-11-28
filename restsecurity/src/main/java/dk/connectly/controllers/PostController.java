package dk.connectly.controllers;

import dk.connectly.daos.PostDAO;
import dk.connectly.daos.SecurityDao;
import dk.connectly.dtos.PostDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Post;

import dk.connectly.model.Role;
import dk.connectly.model.User;
import io.javalin.http.Handler;

import java.util.Objects;

public class PostController {
    private static PostController instance;
    private static PostDAO postDAO;
    private static SecurityDao securityDao;


    private PostController() {

    }

    public static PostController getInstance(boolean isTesting) {
        if (instance == null) {
            instance = new PostController();
            postDAO = PostDAO.getInstance(isTesting);
            securityDao = SecurityDao.getInstance(isTesting);
        }
        return instance;
    }



    public Handler createPost() {
        return (ctx) -> {
            try {
                PostDTO postDTO = ctx.bodyAsClass(PostDTO.class);
                User author = securityDao.getById(postDTO.getAuthor());

                Post newPost = new Post(postDTO, author);
                postDAO.create(newPost);

                PostDTO postDTOToBeShowed = new PostDTO(newPost);
                ctx.status(201);
                ctx.json(postDTOToBeShowed);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while creating post" + e.getMessage());
            }
        };
    }


    public Handler getPostsByVisibility () {
        return (ctx) -> {
            try {
                UserDTO userDTO  = ctx.bodyAsClass(UserDTO.class);
                User user = securityDao.getById(userDTO.getEmail());
                String visibility = ctx.queryParam("visibility");
                int page = Integer.parseInt(Objects.requireNonNullElse(ctx.queryParam("page"), "1"));
                int size = Integer.parseInt(Objects.requireNonNullElse(ctx.queryParam("size"), "10"));
                ctx.json(postDAO.getPostsByVisibility(visibility, user,page, size));

            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while getting posts " + e.getMessage());
            }
        };
    }
}
