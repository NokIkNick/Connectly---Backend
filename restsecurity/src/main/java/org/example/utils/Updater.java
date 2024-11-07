package org.example.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Updater {

    public static <E, DTO> E updateFromDTO(E entity,DTO DTO){
        // Set of field names to skip during update should be sat as a private static attribute
        // on my update class and when I make a generic iteration of it.
        final Set<String> fieldsToSkip = new HashSet<>(Arrays.asList("id"));
        // gets the class of my dto
        Class<?> dtoClass = DTO.getClass();
        // gets all the declared fields in my dto class and stores then in a Field array.
        Field[] dtoFields = dtoClass.getDeclaredFields();
        /*
        I loop through the field array and gets the name of each field.
         */
        for (Field dtoField: dtoFields) {
            if(!dtoField.isSynthetic() && !fieldsToSkip.contains(dtoField.getName())){
                String dtoFieldName = dtoField.getName();
                try{
                    /*
                    finds the hotel field with the same name as the dto field. and throws
                    a no such field exception if none is matching.
                     */
                    Field hotelField = entity.getClass().getDeclaredField(dtoFieldName);
                    // makes it possible to access and modify the fields.
                    hotelField.setAccessible(true);
                    dtoField.setAccessible(true);
                    // if the 2 fields types are equal it gets the values stores on the dto on the field otherwise it skips.
                    // could be replaced with if (hotelField.getType().isAssignableFrom(dtoField.getType()))
                    if (dtoField.getType().equals(hotelField.getType())) {
                        Object dtoFieldValue = dtoField.get(DTO);
                        // if the value gotten is not null it sets the hotels field to the value of the dto field.
                        if (dtoFieldValue != null && !dtoFieldValue.equals(0)) {
                            hotelField.set(entity, dtoFieldValue);
                        }
                    }
                }catch (Exception e){
                    return null;
                }
            }
        }
        return entity;
    }
}