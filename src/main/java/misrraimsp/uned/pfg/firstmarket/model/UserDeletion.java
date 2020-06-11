package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.data.Auditable;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class UserDeletion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private DeletionReason deletionReason;

    private String comment;

}
