package dk.connectly.controllers;

import dk.connectly.daos.PostDAO;
import dk.connectly.dtos.PostDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Post;

import dk.connectly.model.User;
import io.javalin.http.Handler;

import java.util.Objects;

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



    public Handler createPost() {
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


    public Handler getPostsByVisibility () {
        return (ctx) -> {
            int size = 10;
            try {

                //User user = new User( "password","user1@example.com");
                User user = ctx.sessionAttribute("user");
                String visibility = ctx.queryParam("visibility");
                int page = Integer.parseInt(Objects.requireNonNullElse(ctx.queryParam("page"), "1"));
                ctx.json(postDAO.getPostsByVisibility(visibility, user,page, size));

            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500 ,"Error while getting posts" + e.getMessage());
            }
        };
    }
}
