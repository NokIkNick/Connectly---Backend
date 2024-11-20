package dk.connectly.controllers;

import dk.connectly.daos.PostDAO;
import dk.connectly.dtos.PostDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Post;

import dk.connectly.model.User;
import io.javalin.http.Handler;

public class PostController {
    private static PostController instance;
    private static PostDAO postDAO;


    private PostController() {

    }

    public static PostController getInstance() {
        if (instance == null) {
            instance = new PostController();
            postDAO = PostDAO.getInstance();
        }
        return instance;
    }



    public static Handler createPost() {
        return (ctx) -> {
            try {
                PostDTO postDTO = ctx.bodyAsClass(PostDTO.class);
                User postAuthor = ctx.sessionAttribute("user");
                Post newPost = new Post(postDTO, postAuthor);
                postDAO.create(newPost);
                ctx.status(201);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while creating post" + e.getMessage());
            }
        };
    }


    public static Handler getPostsByVisibility () {
        return (ctx) -> {
            try {
                String visibility = ctx.pathParam("visibility");
                User user = ctx.sessionAttribute("user");
                ctx.json(postDAO.getPostsByVisibility(visibility, user));
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while getting posts" + e.getMessage());
            }
        };
    }
}
