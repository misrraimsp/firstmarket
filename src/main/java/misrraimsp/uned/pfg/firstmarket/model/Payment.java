package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public String getFormattedAmount() {
        BigDecimal bd = BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
        return bd.toString();
    }
}
