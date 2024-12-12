package dk.connectly.controllers;

import dk.connectly.daos.ConnectionRequestDAO;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.NewConnectionDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.ConnectionRequest;
import dk.connectly.utils.TokenUtils;

import java.text.ParseException;
import java.util.List;

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

	public Handler getMyRequests() {
    return (ctx) -> {
      ObjectNode returnObject = objectMapper.createObjectNode();
      try {
        UserDTO userDTO = tokenUtils.getUserWithRolesFromToken(ctx.header("Authorization").split(" ")[1]);
        ctx.status(HttpStatus.CREATED).json(getRequestsTo(userDTO));
      }catch(ParseException e){
        ctx.status(HttpStatus.BAD_REQUEST);
        ctx.json(returnObject.put("msg", e.getMessage() + " Relog."));
      }
    };
	}

  public Handler getRequestsOf(){
    return (ctx) -> {
      UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
      ctx.status(HttpStatus.CREATED).json(getRequestsTo(userDTO));
    };
  }


  private List<ConnectionRequestDTO> getRequestsTo(UserDTO userDTO){
    List<ConnectionRequest> conRequest = connectionRequestDAO.getByReciever(userDTO);
    return conRequest.stream().map((cr) -> new ConnectionRequestDTO(cr)).toList();
  }
}
