package misrraimsp.uned.pfg.firstmarket.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.ProductStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class Product extends PriceEntity {

    @Enumerated(value = EnumType.STRING)
    protected ProductStatus status;

    protected int stock;

    protected int year;

    protected String description;

    @ManyToOne
    protected Category category;

    @ManyToOne
    protected Image image;
}
