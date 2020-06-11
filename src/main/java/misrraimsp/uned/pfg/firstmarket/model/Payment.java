package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.data.Auditable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Payment extends Auditable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String stripePaymentIntentId;

    private Long amount;

    private String currency;

    private String description;

    public String getFormattedAmount() {
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP).toString();
    }
}
