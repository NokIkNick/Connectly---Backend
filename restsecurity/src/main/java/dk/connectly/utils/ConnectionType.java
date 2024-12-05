package dk.connectly.utils;

import lombok.Getter;

@Getter
public enum ConnectionType implements OrdinalEnum<ConnectionType> {
  WORK,
  FAMILY,
  FRIEND,
  PUBLIC;

  @Override
  public String toString() {
    return name();
  }
}