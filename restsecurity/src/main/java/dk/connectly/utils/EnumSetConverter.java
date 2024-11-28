package dk.connectly.utils;

import java.util.Set;
import java.util.function.Supplier;

import jakarta.persistence.AttributeConverter;

public class EnumSetConverter<T extends OrdinalEnum<T>> implements AttributeConverter<Set<T>, Integer>{
  private final Supplier<T[]> supplier;

  EnumSetConverter(Supplier<T[]> supplier){
    this.supplier = supplier;
  }

  @Override
  public Integer convertToDatabaseColumn(Set<T> attribute) {
    return attribute.stream()
      // convert to the ordinals (array index of enum (0,1,2,3...))
      .mapToInt(T::ordinal)
      // convert to enum index also called binary index (1,2,4,8...)
      .map(x -> 2^x)
      // take the sum (0 and 2 = 5 (1+4))
      .reduce(Integer::sum).getAsInt();
  }

  @Override
  public Set<T> convertToEntityAttribute(Integer dbData) {
    // Initialise empty Set
    Set<T> output = Set.of();

    // Get the Ordinals (0,1,2,3...)
    for (T value : supplier.get()) {
      // the & operator compares the enum/binary index and the value, which is the enum index
      if((dbData & (int) Math.pow(2,value.ordinal())) != 0){
        output.add(value);
      }
    }
    return output;
  }

}
