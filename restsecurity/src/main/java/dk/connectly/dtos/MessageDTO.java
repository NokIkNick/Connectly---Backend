package dk.connectly.dtos;

import lombok.*;
import org.bson.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class MessageDTO {

    private Long chat_id;
    private String messenger;
    private String message;
    private String sent_at;

    public MessageDTO(Document created){
        if(created.get("chat_id") instanceof Integer){
            this.chat_id = (long) (int) created.get("chat_id");
        }else {
            this.chat_id = created.getLong("chat_id");
        }
        this.messenger = created.getString("messenger");
        this.message = created.getString("message");
        this.sent_at = created.getString("sent_at");
    }

}
