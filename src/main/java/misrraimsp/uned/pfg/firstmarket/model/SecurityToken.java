package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.event.security.SecurityEvent;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class SecurityToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private SecurityEvent securityEvent;

    //@OneToOne(fetch = FetchType.EAGER)
    //@JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    private Date expiryDate;

    private String editedEmail;

}