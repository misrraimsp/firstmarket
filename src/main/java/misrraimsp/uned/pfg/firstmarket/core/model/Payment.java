package misrraimsp.uned.pfg.firstmarket.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Payment extends BasicEntity {

    private String stripePaymentIntentId;

    private Long amount;

    private String currency;

    private String description;

    public String getFormattedAmount() {
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP).toString();
    }
}
