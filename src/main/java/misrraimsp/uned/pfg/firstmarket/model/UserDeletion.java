package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class UserDeletion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private Date date;

    private DeletionReason deletionReason;

    private String comment;

}
