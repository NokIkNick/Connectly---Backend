package org.example.controllers;

import org.example.daos.ConnectionRequestDAO;
import org.example.dtos.NewConnectionDTO;
import org.example.dtos.TokenDTO;
import org.example.dtos.UserDTO;
import org.example.exceptions.ApiException;
import org.example.model.User;
import org.example.utils.TokenUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityExistsException;

public class ConnectionRequestController {
  private static ObjectMapper objectMapper = new ObjectMapper();
  private static TokenUtils tokenUtils = new TokenUtils();
  private static ConnectionRequestDAO connectionRequestDAO;
  private static ConnectionRequestController instance;

  public static ConnectionRequestController getInstance(boolean isTesting){
      if(instance == null){
          instance = new ConnectionRequestController();
          connectionRequestDAO = ConnectionRequestDAO.getInstance(isTesting);
      }
      return instance;
  }

  public Handler setupRequest(){
    return (ctx) -> {
      ObjectNode returnObject = objectMapper.createObjectNode();
      try{
        NewConnectionDTO connectionDTO = ctx.bodyAsClass(NewConnectionDTO.class);
        UserDTO connector = tokenUtils.getUserWithRolesFromToken(ctx.header("Authorization"));
        connectionRequestDAO.setupNewRequest(connector, connectionDTO.connection, connectionDTO.connectionTypes);

        ctx.status(HttpStatus.CREATED).json(connector);
      }catch(EntityExistsException | ApiException e){
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
        ctx.json(returnObject.put("msg", "User already exists"));
      }
    };
  }
}
