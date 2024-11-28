package dk.connectly.dtos;

import java.util.Set;

import dk.connectly.utils.ConnectionType;
import dk.connectly.utils.ConnectionTypeSetConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class NewConnectionDTO {
  public UserDTO connection;
  
  @Convert(converter = ConnectionTypeSetConverter.class)
  public Set<ConnectionType> connectionTypes;
}
