package dk.connectly.dtos;


import lombok.*;
import org.bson.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ChatDTO implements DTO<Long>{

    private Long _id;
    private List<String> participants;
    private String created_at;
    private String last_message_at;


    public ChatDTO(Document created) {
        this._id = created.getLong("_id");
        this.participants = created.getList("participants", String.class);
        this.created_at = created.getString("created_at");
        this.last_message_at = created.getString("last_message_at");
    }

    @Override
    public Long getId() {
        return _id;
    }

    @Override
    public void setId(Long id) {
        this._id = id;
    }
}
