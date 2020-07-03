package misrraimsp.uned.pfg.firstmarket.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class PriceEntity extends BasicEntity {

    protected BigDecimal price;
}
