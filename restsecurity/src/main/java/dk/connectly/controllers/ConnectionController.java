package dk.connectly.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dk.connectly.daos.ConnectionDAO;
import dk.connectly.dtos.ConnectionDTO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Connection;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityExistsException;
import dk.connectly.utils.TokenUtils;

import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionController {
  private static ObjectMapper objectMapper = new ObjectMapper();
  private static TokenUtils tokenUtils = new TokenUtils();
  private static ConnectionDAO connectionDAO;
  private static ConnectionController instance;

  public static ConnectionController getInstance(boolean isTesting){
      if(instance == null){
          instance = new ConnectionController();
          connectionDAO = ConnectionDAO.getInstance(isTesting);
      }
      return instance;
  }

  public Handler acceptRequest() {
    return (ctx) -> {
      ObjectNode returnObject = objectMapper.createObjectNode();
      try{
        ConnectionRequestDTO CRDTO = ctx.bodyAsClass(ConnectionRequestDTO.class);
        UserDTO userDTO = getUserFromHeader(ctx);
        if(userDTO.getEmail() != CRDTO.getReceiver().getEmail()){
          ctx.status(HttpStatus.BAD_REQUEST);
        }
        ConnectionDTO DTO = connectionDAO.acceptRequest(CRDTO);

        ctx.status(HttpStatus.CREATED).json(DTO);
      }catch(EntityExistsException | ApiException e){
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
        ctx.json(returnObject.put("msg", e.getMessage() + " already exists"));
      }
    };
  }

  public Handler getAllIAmConnectedTo(){
    return (ctx) -> {
      UserDTO userDTO = getUserFromHeader(ctx);
      getConnectionsTo(userDTO, ctx);
    };
  }

  public Handler getAllConnectionsTo() {
    return (ctx) -> {
      UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
      getConnectionsTo(userDTO, ctx);
    };
  }

  private UserDTO getUserFromHeader(Context ctx) throws ParseException {
    if(ctx.header("Authorization") == null || ctx.header("Authorization").split(" ").length !=2){
      ctx.status(HttpStatus.BAD_REQUEST);
    }
    String token = ctx.header("Authorization").split(" ")[1];
    UserDTO connector = tokenUtils.getUserWithRolesFromToken(token);
    return connector;
  }

  private void getConnectionsTo(UserDTO userDTO, Context ctx){
    List<Connection> conns = connectionDAO.getAllIAmConnectedTo(userDTO);
    ctx.status(HttpStatus.OK).json(conns);
  }
}
