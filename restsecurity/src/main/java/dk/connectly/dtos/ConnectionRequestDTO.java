package dk.connectly.dtos;

import lombok.*;

import java.util.Set;

import dk.connectly.model.ConnectionRequest;
import dk.connectly.utils.ConnectionType;
import dk.connectly.utils.ConnectionTypeSetConverter;
import jakarta.persistence.Convert;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionRequestDTO implements DTO<Integer> {
    Integer id;
    UserDTO requester;
    UserDTO receiver;
    
    @Convert(converter = ConnectionTypeSetConverter.class)
    Set<ConnectionType> types;

    public ConnectionRequestDTO(UserDTO requester, UserDTO receiver, Set<ConnectionType> types){
        this.requester = requester;
        this.receiver = receiver;
        this.types = types;
    }

    public ConnectionRequestDTO(ConnectionRequest CR){
        id = CR.getId();
        requester = new UserDTO(CR.getRequester());
        receiver = new UserDTO(CR.getReceiver());
        types = CR.getTypes();
    }
}
