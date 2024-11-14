package org.example.dtos;

import java.util.Set;

import org.example.utils.ConnectionType;

public class NewConnectionDTO {
  public UserDTO connection;
  public Set<ConnectionType> connectionTypes;
}
