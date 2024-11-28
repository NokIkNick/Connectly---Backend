package dk.connectly.controllers;

import dk.connectly.daos.ConnectionRequestDAO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.NewConnectionDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.utils.TokenUtils;

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
        UserDTO connector = tokenUtils.getUserWithRolesFromToken(ctx.header("Authorization").split(" ")[1]);
        ConnectionRequestDTO DTO = connectionRequestDAO.setupNewRequest(connector, connectionDTO.connection, connectionDTO.connectionTypes);

        ctx.status(HttpStatus.CREATED).json(DTO);
      }catch(EntityExistsException | ApiException e){
        ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
        ctx.json(returnObject.put("msg", e.getMessage() + " already exists"));
      }
    };
  }
}
