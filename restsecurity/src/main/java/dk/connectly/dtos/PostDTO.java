package dk.connectly.dtos;


import dk.connectly.model.Post;
import dk.connectly.model.User;
import dk.connectly.utils.ConnectionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

        private Integer id;
        private String author;
        private Date date_created;
        private String title;
        private String content;
        private ConnectionType visibility;


        public PostDTO(Post post) {
            this.id = post.getId();
            this.author = post.getAuthor().getEmail();
            this.date_created = post.getDate_created();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.visibility = post.getVisibility();
        }


}
