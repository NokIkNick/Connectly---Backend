package dk.connectly.utils;

public class testEnemSetConverter extends EnumSetConverter<ConnectionType>{
  public testEnemSetConverter(){
    super(() -> ConnectionType.values());
  }
}
