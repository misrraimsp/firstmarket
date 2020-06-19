package misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort;

import org.springframework.data.domain.Sort;

public enum OrderSortCriteria {

    CREATED_DATE_ASC("Less recent", "createdDate", Sort.Direction.ASC),
    CREATED_DATE_DESC("Most recent", "createdDate", Sort.Direction.DESC),
    STATUS_ASC ("Status a-z", "status", Sort.Direction.ASC),
    STATUS_DESC ("Status z-a", "status", Sort.Direction.DESC),
    USER_ASC ("User a-z", "user.email", Sort.Direction.ASC),
    USER_DESC ("User z-a", "user.email", Sort.Direction.DESC),
    AMOUNT_ASC ("Price increasing", "payment.amount", Sort.Direction.ASC),
    AMOUNT_DESC ("Price decreasing", "payment.amount", Sort.Direction.DESC);

    private final String text;
    private final String property;
    private final Sort.Direction direction;

    OrderSortCriteria(String text, String property, Sort.Direction direction) {
        this.text = text;
        this.property = property;
        this.direction = direction;
    }

    public String getText() {
        return text;
    }

    public String getProperty() {
        return property;
    }

    public Sort.Direction getDirection() {
        return direction;
    }
}
