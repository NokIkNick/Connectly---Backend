package dk.connectly.dtos;

import java.util.Set;

import dk.connectly.utils.ConnectionType;
import dk.connectly.utils.ConnectionTypeSetConverter;
import jakarta.persistence.Convert;

public class NewConnectionDTO {
  public UserDTO connection;
  
  @Convert(converter = ConnectionTypeSetConverter.class)
  public Set<ConnectionType> connectionTypes;
}
