package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class SecurityToken extends BasicEntity {

    private String token;

    @Enumerated(EnumType.STRING)
    private SecurityEvent securityEvent;

    @ManyToOne
    private User targetUser;

    private String targetEmail;

}