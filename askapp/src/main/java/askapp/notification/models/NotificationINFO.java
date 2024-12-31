package askapp.notification.models;


import askapp.auth.Userinfo;
import askapp.post.Models.ModelsINFO.PostINFO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationINFO {
    private long id;
    private PostINFO post;
    private Userinfo whoreact;
    private String user;
    private Type type;
    private Date date;
    private Boolean seen;
}
