package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;

import javax.persistence.*;

@Data
@Entity
public class SecurityToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private SecurityEvent securityEvent;

    @ManyToOne
    private User user;

    private String expiryDate;

    private String editedEmail;

}