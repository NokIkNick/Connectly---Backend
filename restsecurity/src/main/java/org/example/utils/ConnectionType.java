package org.example.utils;

import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public enum ConnectionType {
  WORK("Work"),
  FAMILY("Family"),
  Friend("Friend");

  private final String typeName;

  //byte -128 to 127, smallest inbuilt Java number - TINYINT in databases
  @Id
  private final byte internalId;

  ConnectionType(String typeName){
    this.typeName = typeName;
    internalId = (byte) this.ordinal();
  }

  @Override
  public String toString() {
    return typeName;
  }
}
