package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.data.Auditable;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class SecurityToken extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private SecurityEvent securityEvent;

    @ManyToOne
    private User targetUser;

    private String targetEmail;

}