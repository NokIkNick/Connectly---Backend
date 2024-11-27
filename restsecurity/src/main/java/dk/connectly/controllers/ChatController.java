package dk.connectly.controllers;

import dk.connectly.dtos.ChatDTO;
import dk.connectly.dtos.MessageDTO;
import dk.connectly.exceptions.ApiException;
import io.javalin.http.Handler;
import org.bson.Document;

import java.util.List;

public class ChatController {

    private static ChatController instance;

    private static ChatServiceDAO chatServiceDAO;

    public static ChatController getInstance(boolean isTesting){
        if(instance == null){
            instance = new ChatController();
            chatServiceDAO = ChatServiceDAO.getInstance(isTesting);
        }
        return instance;
    }

    private ChatController(){
    }

    public static Handler createChat(){
        return ctx -> {
            try{
                ChatDTO toCreateDTO = ctx.bodyAsClass(ChatDTO.class);
                Document created = chatServiceDAO.createChat(toCreateDTO.getParticipants().get(0), toCreateDTO.getParticipants().get(1));
                ChatDTO toReturn = new ChatDTO(created);
                ctx.json(toReturn);
            }catch(Exception e){
                ctx.status(500);
                throw new ApiException(500, "Error while creating new chat: "+e.getMessage());
            }
        };
    }

    public static Handler fetchChat(){
        return ctx -> {
            try {
                ChatDTO toFetchDTO = ctx.bodyAsClass(ChatDTO.class);
                Document fetched = chatServiceDAO.fetchChat(toFetchDTO.getParticipants().get(0), toFetchDTO.getParticipants().get(1));
                ChatDTO toReturn = new ChatDTO(fetched);
                ctx.json(toReturn);

            }catch (Exception e){
                ctx.status(500);
                throw new ApiException(500, "Error while fetching chat: " + e.getMessage());
            }
        };
    }

    public static Handler fetchChatById(){
        return ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                Document fetched = chatServiceDAO.fetchChatById(id);
                ChatDTO toReturn = new ChatDTO(fetched);
                ctx.json(toReturn);

            }catch (Exception e){
                ctx.status(500);
                throw new ApiException(500, "Error while fetching chat: " + e.getMessage());
            }
        };
    }

    public static Handler sendMessage(){
        return ctx -> {
            try {
                MessageDTO toCreate = ctx.bodyAsClass(MessageDTO.class);
                Document sent = chatServiceDAO.sendMessage(toCreate.getChat_id(), toCreate.getMessenger(), toCreate.getMessage());
                MessageDTO toReturn = new MessageDTO(sent);
                ctx.json(toReturn);
            }catch (Exception e){
                ctx.status(500);
                throw new ApiException(500, "Error while sending message: "+e.getMessage());
            }
        };
    }

    public static Handler getMessagesByChatId(){
        return ctx -> {
            try {
                long id = Long.parseLong(ctx.pathParam("id"));
                List<MessageDTO> messageDTOS = chatServiceDAO.fetchMessagesByChatId(id).stream().map((MessageDTO::new)).toList();
                ctx.json(messageDTOS);
            }catch (Exception e){
                ctx.status(500);
                throw new ApiException(500, "Error while fetching messages: "+e.getMessage());
            }
        };
    }

    public static Handler getChatsByUser(){
        return ctx -> {
            try {
                String userEmail = ctx.pathParam("email");
                List<ChatDTO> chatDTOS = chatServiceDAO.listChatsForUser(userEmail).stream().map(ChatDTO::new).toList();
                ctx.json(chatDTOS);
            }catch (Exception e){
                ctx.status(500);
                throw new ApiException(500, "Error while fetching messages: "+e.getMessage());
            }
        };
    }
}
