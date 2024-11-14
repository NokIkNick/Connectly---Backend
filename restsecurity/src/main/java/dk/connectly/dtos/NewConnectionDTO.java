package dk.connectly.dtos;

import java.util.Set;

import dk.connectly.utils.ConnectionType;

public class NewConnectionDTO {
  public UserDTO connection;
  public Set<ConnectionType> connectionTypes;
}
