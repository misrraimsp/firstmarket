package misrraimsp.uned.pfg.firstmarket.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class UserDeletion extends BasicEntity {

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private DeletionReason deletionReason;

    private String comment;

}
