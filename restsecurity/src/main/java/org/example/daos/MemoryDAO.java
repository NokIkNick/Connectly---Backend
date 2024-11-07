package org.example.daos;

import org.example.dtos.DTO;
import org.example.exceptions.ApiException;
import org.example.utils.Updater;

import java.util.*;

public class MemoryDAO<T extends DTO<Integer>, K> implements IDAO<T, K>{

    private Set<T> memory = new LinkedHashSet<>();
    private static int idCounter = 0;

    @Override
    public List<T> getAll() throws ApiException {
        if(memory.isEmpty()){
            throw new ApiException(404,"Memory storage is empty");
        }
        return memory.stream().toList();
    }

    @Override
    public T getById(K id) throws ApiException {
        if(memory.isEmpty()){
            throw new ApiException(404,"Memory storage is empty");
        }
        if(Integer.parseInt(String.valueOf(id)) > idCounter){
            throw new ApiException(404,"Id is greater than current memory size");
        }
        T found = memory.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if(found == null){
            throw new ApiException(404,"Not found");
        }
        return found;
    }

    @Override
    public T create(T in) throws ApiException {
        if(in != null) {
            if(!memory.contains(in)) {
                idCounter++;
                in.setId(idCounter);
                memory.add(in);
            }else {
                throw new ApiException(422,in.getId()+"Object of key: "+in.getId()+" already exists in memory");
            }
        }
        return in;
    }

    @Override
    public T update(T in, K id) throws ApiException {
        if(Integer.parseInt(String.valueOf(id)) > idCounter){
            throw new ApiException(404,"Key value is greater than current memory size");
        }
        T original = getById(id);
        if(original == null){
            throw new ApiException(404,"Not found");
        }
        T updated = Updater.updateFromDTO(original, in);
        memory.remove(original);
        memory.add(updated);
        return updated;
    }

    @Override
    public T delete(K id) throws ApiException {
        T found;
        T toRemove = null;
        if(id != null){
            if(Integer.parseInt(String.valueOf(id)) > idCounter){
                throw new ApiException(404,"Key value is greater than current memory size");
            }
            found = getById(id);
            if(found != null){
                toRemove = memory.stream().filter(x -> Objects.equals(x.getId(), found.getId())).findFirst().orElse(null);
                memory.remove(toRemove);
            }else {
                throw new ApiException(404,"Not found");
            }
        }
        return toRemove;
    }
}
