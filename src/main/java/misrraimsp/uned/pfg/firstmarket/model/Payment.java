package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String stripePaymentIntentId;

    private Long amount;

    private String currency;

    private String description;
}
