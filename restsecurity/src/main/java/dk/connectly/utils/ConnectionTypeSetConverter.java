package dk.connectly.utils;

import java.util.Set;

import jakarta.persistence.AttributeConverter;

public class ConnectionTypeSetConverter implements AttributeConverter<Set<ConnectionType>, Integer>{
  @Override
  public Integer convertToDatabaseColumn(Set<ConnectionType> attribute) {
    return attribute.stream()
      // convert to the ordinals (array index of enum (0,1,2,3...))
      .mapToInt(ConnectionType::ordinal)
      // convert to enum index also called binary index (1,2,4,8...)
      .map(x -> 2^x)
      // take the sum (0 and 2 = 5 (1+4))
      .reduce(Integer::sum).getAsInt();
  }

  @Override
  public Set<ConnectionType> convertToEntityAttribute(Integer dbData) {
    // Initialise empty Set
    Set<ConnectionType> output = Set.of();

    // Get the Ordinals (0,1,2,3...)
    for (ConnectionType value : ConnectionType.values()) {
      // the & operator compares the enum/binary index and the value, which is the enum index
      if((dbData & (int) Math.pow(2,value.ordinal())) != 0){
        output.add(value);
      }
    }
    return output;
  }
}
